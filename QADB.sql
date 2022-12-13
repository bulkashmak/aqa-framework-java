BEGIN
TRANSACTION;

CREATE TABLE "Users"
(
    "id"    INTEGER,
    "phone" INTEGER /*UNIQUE*/,
    "absId" INTEGER,
    "name"  TEXT UNIQUE,
    PRIMARY KEY ("id" AUTOINCREMENT)
);


/*
To use generators for Phone use:
    INSERT INTO Users (Name) VALUES ('<name>');
To do Not use generator for Phone use:
    INSERT INTO Users (Name, Phone) VALUES ('<name>', <phone>);
*/

CREATE TRIGGER phone_email_generators
    AFTER INSERT
    ON users
BEGIN
    UPDATE users
    SET Phone = NEW.id + 998331000000
    WHERE id = NEW.id
      AND NEW.Phone ISNULL;
END;

INSERT INTO Users (name, phone, absId)
VALUES ('b2cAuthTests', 998909543300, 99055213);

INSERT INTO Users (name, phone, absId)
VALUES ('b2cProfileTests', 998909543300, 99055213);

INSERT INTO Users (name, phone, absId)
VALUES ('b2cAccountsTests1', 998909543300, 99055213);

INSERT INTO Users (name, phone, absId)
VALUES ('b2cAccountsTests2', 998931623221, 99000217);

INSERT INTO Users (name, phone, absId)
VALUES ('b2cAccountTransactionsTests1', 998931623221, 99000217);

INSERT INTO Users (name, phone, absId)
VALUES ('b2cCardsTests', 998931623221, 99000217);

INSERT INTO Users (name, phone, absId)
VALUES ('bankProductsFavoritePaymentsTests1', 998931623221, 99000217);

INSERT INTO Users (name, phone, absId)
VALUES ('bankProductsFavoritePaymentsTests2', 998909543300, 99055213);


COMMIT;
