CREATE OR REPLACE VIEW v_products_recommended_by_at_least_one_user AS
SELECT DISTINCT product
FROM review_head
WHERE recommended = true
AND value_for_price > 3;

CREATE OR REPLACE VIEW v_favourite_product_of_each_user_from_each_category
AS
WITH user_rating_averages AS (
		SELECT "user", category, product, AVG(CAST(rb.score AS NUMERIC)) AS average
		FROM review_body AS rb
		INNER JOIN product p ON rb.product = p.id
		INNER JOIN article a ON p.article = a.id
		GROUP BY "user", category, product
	),
	user_best_ratings AS (
		SELECT "user", category, product, average,
			RANK() OVER (PARTITION BY "user", category ORDER BY average DESC) as ranking
		FROM user_rating_averages
	)
	SELECT *
	FROM user_best_ratings
	WHERE ranking = 1;

	CREATE OR REPLACE VIEW v_how_many_percent_of_users_buy_mostly_domestic_products AS
    WITH users_with_product AS (
        SELECT
            rh."user",
            b.country AS product_country,
            rh.product,
            u.country AS user_country
        FROM review_head rh
        INNER JOIN product p ON rh.product = p.id
        INNER JOIN article a ON p.article = a.id
        INNER JOIN brand b ON a.brand = b.id
        INNER JOIN "user" u ON rh."user" = u.id
    ),
    users_with_domestic_product AS (
        SELECT "user", COUNT(product) AS domestic_products
        FROM users_with_product
        WHERE user_country = product_country
        GROUP BY "user"
    ),
    users_total_products AS (
        SELECT "user", COUNT(product) AS total_products
        FROM users_with_product
        GROUP BY "user"
    ),
    users_domestic_percentage AS (
        SELECT
            utp."user",
            CASE
                WHEN total_products = 0 THEN 0
                ELSE domestic_products / NULLIF(CAST(total_products AS NUMERIC), 0)
            END AS domestic_percentage
        FROM users_total_products utp
        INNER JOIN users_with_domestic_product udp ON udp."user" = utp."user"
    ),
    users_domestic_above_50 AS (
        SELECT COUNT("user") AS number_of_users
        FROM users_domestic_percentage
        WHERE domestic_percentage > 0.5
    )
    SELECT
        CASE
            WHEN COUNT(DISTINCT "user") = 0 THEN 0
            ELSE
                (SELECT number_of_users FROM users_domestic_above_50) * 100
                / NULLIF(CAST(COUNT(DISTINCT "user") AS NUMERIC), 0)
        END AS percent
    FROM users_with_product;

    CREATE OR REPLACE VIEW v_most_popular_articles_of_brands AS
    WITH product_rating_averages AS (
        SELECT
            b.name AS brand,
            a.name AS article,
            (COALESCE(AVG(CAST(rb.score AS NUMERIC(10,2))), 5) + rh.value_for_price) / 2 AS average
        FROM review_head AS rh
        LEFT JOIN review_body rb
            ON rb."user" = rh."user" AND rb.product = rh.product
        INNER JOIN product p ON rh.product = p.id
        INNER JOIN article a ON p.article = a.id
        INNER JOIN brand b ON a.brand = b.id
        GROUP BY b.name, a.name, rh.value_for_price
    ),
    product_best_ratings AS (
        SELECT
            brand,
            article,
            average,
            RANK() OVER (PARTITION BY brand ORDER BY average DESC) AS ranking
        FROM product_rating_averages
    )
    SELECT *
    FROM product_best_ratings
    WHERE ranking = 1;

    CREATE OR REPLACE VIEW v_most_popular_articles_of_categories AS
    WITH product_rating_averages AS (
        SELECT
            c.name AS category,
            a.name AS article,
            (COALESCE(AVG(CAST(rb.score AS NUMERIC(10,2))), 5) + rh.value_for_price) / 2 AS average
        FROM review_head AS rh
        LEFT JOIN review_body rb
            ON rb."user" = rh."user" AND rb.product = rh.product
        INNER JOIN product p ON rh.product = p.id
        INNER JOIN article a ON p.article = a.id
        INNER JOIN category c ON a.category = c.id
        GROUP BY c.name, a.name, rh.value_for_price
    ),
    product_best_ratings AS (
        SELECT
            category,
            article,
            average,
            RANK() OVER (PARTITION BY category ORDER BY average DESC) AS ranking
        FROM product_rating_averages
    )
    SELECT category, article, average
    FROM product_best_ratings
    WHERE ranking = 1;

    CREATE OR REPLACE VIEW v_which_packaging_is_the_most_popular_for_each_article AS
    WITH packaging_rating_averages AS (
        SELECT
            a.id AS article_id,
            p.packaging,
            AVG(CAST(rb.score AS NUMERIC(10,2))) AS average
        FROM review_body AS rb
        INNER JOIN product p ON rb.product = p.id
        INNER JOIN article a ON p.article = a.id
        GROUP BY a.id, p.packaging
    ),
    packaging_best_ratings AS (
        SELECT
            article_id,
            packaging,
            average,
            RANK() OVER (PARTITION BY article_id ORDER BY average DESC) AS ranking
        FROM packaging_rating_averages
    )
    SELECT article_id AS article, packaging AS most_popular_packaging
    FROM packaging_best_ratings
    WHERE ranking = 1;

    CREATE OR REPLACE VIEW v_best_rated_products_per_category
    AS
    WITH join_cte AS (
    		SELECT a.category, p.id, AVG(CAST(rb.score AS NUMERIC(10,2)) + rh.value_for_price) AS average
    		FROM review_body AS rb
    		INNER JOIN review_head rh ON rb."user" = rh."user"
    			AND rb.product = rh.product
    		INNER JOIN product p ON rb.product = p.id
    		INNER JOIN article a ON p.article = a.id
    		GROUP BY a.category, p.id
    	),
    	category_best_ratings AS (
    		SELECT category, id, average,
    			RANK() OVER (PARTITION BY category ORDER BY average DESC) as ranking
    		FROM join_cte
    	)
    	SELECT category, id AS most_popular_product
    	FROM category_best_ratings
    	WHERE ranking = 1;

    CREATE OR REPLACE VIEW v_weakness_of_most_popular_products AS
    WITH get_most_popular_products_ratings_cte AS (
        SELECT
            v.most_popular_product,
            aspect,
            AVG(CAST(score AS NUMERIC(10,2))) AS average
        FROM v_best_rated_products_per_category AS v
        INNER JOIN review_body rb
            ON v.most_popular_product = rb.product
        GROUP BY v.most_popular_product, aspect
    ),
    most_popular_weakest_avg AS (
        SELECT
            most_popular_product,
            aspect,
            average,
            RANK() OVER (PARTITION BY most_popular_product ORDER BY average ASC) AS ranking
        FROM get_most_popular_products_ratings_cte
        WHERE average < 4
    )
    SELECT
        gen_random_uuid() AS id,
        p.id AS product_id,
        a.id AS article_id,
        a.name AS article,
        pkg.id AS packaging_id,
        pkg.name AS packaging,
        asp.id AS aspect_id,
        asp.name AS weakest_aspect,
        ROUND(average, 2)::TEXT || '/5' AS average
    FROM most_popular_weakest_avg
    INNER JOIN product p ON most_popular_product = p.id
    INNER JOIN article a ON p.article = a.id
    INNER JOIN packaging pkg ON p.packaging = pkg.id
    INNER JOIN aspect asp ON most_popular_weakest_avg.aspect = asp.id
    WHERE ranking = 1;

    CREATE OR REPLACE VIEW v_user_with_most_ratings AS
    WITH ratings_per_user AS (
        SELECT "user", COUNT(*) AS rating_amount
        FROM review_head
        GROUP BY "user"
    ),
    ranked_ratings AS (
        SELECT "user", rating_amount,
               RANK() OVER (ORDER BY rating_amount DESC) AS ranking
        FROM ratings_per_user
    )
    SELECT rpu."user", u.username, rpu.rating_amount
    FROM ranked_ratings rpu
    INNER JOIN "user" u ON rpu."user" = u.id
    WHERE ranking = 1;

    CREATE OR REPLACE VIEW v_who_rated_multiple_products_negatively_within_one_hour AS
    SELECT "user", "date", "product", recommended
    FROM review_head rh
    WHERE recommended = FALSE
    AND EXISTS (
        SELECT 1
        FROM review_head rh2
        WHERE rh2."user" = rh."user"
        AND rh2.recommended = FALSE
        AND rh2."date" <> rh."date"
        AND ABS(EXTRACT(EPOCH FROM (rh2."date" - rh."date"))) <= 3600
    );

    CREATE OR REPLACE VIEW v_who_had_7_day_rating_streak AS
    WITH consecutive_days AS (
        SELECT "user", "date",
               "date" - INTERVAL '6 DAYS' AS streak_start
        FROM review_head rh
        WHERE EXISTS (
            SELECT 1
            FROM review_head rh2
            WHERE rh."user" = rh2."user"
            AND rh2."date" BETWEEN rh."date" - INTERVAL '6 DAYS' AND rh."date"
            GROUP BY rh2."user", rh2."date"
            HAVING COUNT(DISTINCT rh2."date") = 7
        )
    )
    SELECT DISTINCT "user", streak_start
    FROM consecutive_days;

    CREATE OR REPLACE VIEW v_which_brand_has_the_most_articles AS
    WITH articles_per_brand AS (
        SELECT brand, COUNT(id) AS article_amount
        FROM article
        GROUP BY brand
    )
    SELECT brand, article_amount
    FROM articles_per_brand
    WHERE article_amount = (SELECT MAX(article_amount) FROM articles_per_brand);

    CREATE OR REPLACE VIEW v_which_brand_has_the_most_products
    AS
    	WITH products_per_brand AS (
    		SELECT brand, COUNT(p.id) as product_amount
    		FROM product AS p
    		INNER JOIN article AS a ON p.article = a.id
    		GROUP BY brand
    	)
    	SELECT brand, product_amount
    	FROM products_per_brand
    	WHERE product_amount = (SELECT MAX(product_amount)
    							FROM products_per_brand);

    CREATE OR REPLACE VIEW v_top_3_best_rated_company AS
    WITH brand_product_average AS (
        SELECT a.brand, AVG(CAST(rb.score AS DECIMAL(10,2))) AS rating_average
        FROM review_body AS rb
        INNER JOIN product AS p ON rb.product = p.id
        INNER JOIN article AS a ON p.article = a.id
        GROUP BY a.brand
    ),
    companies_ranked AS (
        SELECT brand, rating_average, RANK() OVER (ORDER BY rating_average DESC) AS ranking
        FROM brand_product_average
    )
    SELECT ranking, b."name", ROUND(rating_average, 2) || ' / 5.0' AS rating_average
    FROM companies_ranked
    INNER JOIN brand AS b ON companies_ranked.brand = b.id
    WHERE ranking < 4;

    CREATE OR REPLACE VIEW v_user_reviewed_all_products_of_a_brand AS
    WITH total_products_per_brand AS (
        SELECT a.brand, COUNT(p.id) AS total_products
        FROM product AS p
        INNER JOIN article AS a ON p.article = a.id
        GROUP BY a.brand
    ),
    total_reviews_per_user_per_brand AS (
        SELECT rb."user", a.brand, COUNT(rb.product) AS rating_amount
        FROM review_body AS rb
        INNER JOIN product AS p ON rb.product = p.id
        INNER JOIN article AS a ON p.article = a.id
        GROUP BY rb."user", a.brand
    )
    SELECT main_table."user", main_table.brand
    FROM total_reviews_per_user_per_brand AS main_table
    WHERE main_table.rating_amount = (
        SELECT total_products
        FROM total_products_per_brand
        WHERE total_products_per_brand.brand = main_table.brand
    );