CREATE TABLE client
(
    client_id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(128),
    email VARCHAR(30),
    passport VARCHAR(30),
    bank_id uuid,
    FOREIGN KEY (bank_id) REFERENCES bank(bank_id)
);

CREATE TABLE credit
(
    credit_id UUID PRIMARY KEY NOT NULL,
    bank_id uuid,
    type varchar(30),
    limit INTEGER,
    percent INTEGER,
    FOREIGN KEY (bank_id) REFERENCES bank(bank_id)
);

CREATE TABLE bank
(
    bank_id UUID PRIMARY KEY NOT NULL,
    name varchar(30)
);

CREATE TABLE credit_offer
(
    credit_offer_id UUID PRIMARY KEY NOT NULL,
    client_id UUID,
    credit_id UUID,
    bank_id uuid,
    amount INTEGER,
    months_to_pay INTEGER,
    balance Integer,
    FOREIGN KEY (client_id) REFERENCES Client(client_id),
    FOREIGN KEY (credit_id) REFERENCES Credit(credit_id),
    FOREIGN KEY (bank_id) REFERENCES bank(bank_id)
);

CREATE TABLE payment_timetable
(
    payment_timetable_id UUID PRIMARY KEY,
    credit_offer_id UUID,
    payment_date date,
    payment_amount Integer,
    repayment_amount Integer,
    percent_repayment_amount Integer
);

CREATE TABLE uuid_map
(
    id INTEGER,
    uuid UUID
);
