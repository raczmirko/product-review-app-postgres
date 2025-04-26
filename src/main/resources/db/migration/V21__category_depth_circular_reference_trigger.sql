CREATE OR REPLACE FUNCTION trg_check_category_depth_and_circular_reference()
RETURNS TRIGGER AS $$
DECLARE
    depth SMALLINT;
    circular_reference BOOLEAN := FALSE;
BEGIN
    WITH RECURSIVE recursive_cte AS (
        SELECT id, parent_category, 1 AS depth
        FROM category
        WHERE id = NEW.id

        UNION ALL

        SELECT c2.id, c2.parent_category, rc.depth + 1
        FROM category c2
        INNER JOIN recursive_cte rc ON c2.id = rc.parent_category
        WHERE rc.depth < 4
    ),
    occurrences_cte AS (
        SELECT id, COUNT(*) AS id_count
        FROM recursive_cte
        GROUP BY id
    ),
    multiple_occurrences AS (
        SELECT CASE
            WHEN MAX(id_count) = 1 THEN FALSE
            ELSE TRUE
        END AS result
        FROM occurrences_cte
    )
    SELECT
        o.result,
        (SELECT MAX(rcte.depth) FROM recursive_cte rcte)
    INTO
        circular_reference, depth
    FROM multiple_occurrences o;

    IF circular_reference THEN
        RAISE EXCEPTION 'Circular references are not allowed.';
    ELSIF depth > 3 THEN
        RAISE EXCEPTION 'Category hierarchy can not exceed 3 in depth.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_category_depth_and_circular_reference
AFTER INSERT OR UPDATE ON category
FOR EACH ROW
EXECUTE FUNCTION trg_check_category_depth_and_circular_reference()