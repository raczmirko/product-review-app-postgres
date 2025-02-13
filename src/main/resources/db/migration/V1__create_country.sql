CREATE TABLE "country" (
	country_code char(3) PRIMARY KEY,
	"name" varchar(100) NOT NULL,
	CONSTRAINT uq_country_name UNIQUE("name")
);