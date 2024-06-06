create database evs;
-- drop database evs;  

use evs;

create table voters (
    cnic VARCHAR(14) PRIMARY KEY,
    name VARCHAR(255),
    pin VARCHAR(6),
    age INT
);

create table parties (
    id int auto_increment primary key,
    name varchar(255),
    logo varchar(255),
    chairman varchar(14) not null, -- Changed to VARCHAR to match voters table
    foreign key (chairman) references voters(cnic)
); 

create table candidates (
    cnic varchar(14), -- Changed to VARCHAR to match voters table
    party int,
    primary key(cnic, party), -- Composite primary key
    foreign key(cnic) references voters(cnic),
    foreign key(party) references parties(id)
);

create table vote (
	candidate_cnic VARCHAR(14),
    voter_cnic VARCHAR(14) unique,
    vote_at timestamp,
    primary key(candidate_cnic, voter_cnic),
    foreign key(candidate_cnic) references candidates(cnic) on update cascade on delete cascade,
    foreign key(voter_cnic) references voters(cnic)
);

create table admins(
	adminID VARCHAR(6) PRIMARY KEY,
    adminName VARCHAR(255),
    pin VARCHAR(6)
);

-- Sample data for voters
INSERT INTO voters (cnic, name, pin, age) VALUES
('12345678901234', 'Ali Khan', '123456', 35),
('23456789012345', 'Sara Ahmed', '234567', 28),
('34567890123456', 'Ahmed Hassan', '345678', 42),
('45678901234567', 'Fatima Khan', '456789', 30),
('56789012345678', 'Usman Ali', '567890', 39),
('67890123456789', 'Ayesha Mahmood', '678901', 25),
('78901234567890', 'Ahmed Khan', '789012', 50),
('89012345678901', 'Sanaullah Khan', '890123', 36),
('90123456789012', 'Aisha Malik', '901234', 29),
('01234567890123', 'Zubair Baloch', '012345', 48),
('12345098765432', 'Fahad Ahmed', '543210', 33),
('23456109876543', 'Sadia Khan', '654321', 31),
('34567210987654', 'Bilal Akhtar', '765432', 37),
('45678321098765', 'Zainab Ali', '876543', 26),
('56789432109876', 'Imran Ahmed', '987654', 40),
('67890543210987', 'Maryam Khan', '098765', 32),
('78901654321098', 'Khalid Malik', '109876', 45),
('89012765432109', 'Nazia Butt', '210987', 27),
('90123876543210', 'Naeem Khan', '321098', 38),
('01234987654321', 'Amina Ahmed', '432109', 34),
('12345098765098', 'Omar Khalid', '543209', 41),
('23456109876109', 'Bushra Ali', '654320', 29),
('34567210987210', 'Farhan Khan', '765431', 36),
('45678321098321', 'Sadia Akram', '876542', 33),
('56789432109432', 'Tariq Mehmood', '987653', 47),
('67890543210543', 'Ayesha Siddiqui', '098764', 28),
('78901654321654', 'Kamran Mahmood', '109875', 39),
('89012765432765', 'Amna Ali', '210986', 30),
('90123876543876', 'Usman Butt', '321097', 44),
('01234987654987', 'Maria Khan', '432108', 31),
('12345098765001', 'Hassan Ahmed', '543201', 35),
('23456109876112', 'Sanaullah Butt', '654312', 48),
('34567210987223', 'Nida Akram', '765423', 27),
('45678321098334', 'Imran Khan', '876534', 42),
('56789432109445', 'Zahra Ali', '987645', 29),
('67890543210556', 'Samiullah Khan', '098756', 37),
('78901654321667', 'Asma Malik', '109867', 26),
('89012765432778', 'Bilal Ahmed', '210978', 39),
('90123876543889', 'Sadia Khan', '321089', 33),
('01234987654990', 'Ayesha Malik', '432100', 40),
('12345098765011', 'Ahmed Ali', '543211', 30),
('23456109876122', 'Sobia Akram', '654322', 45),
('34567210987233', 'Nadeem Khan', '765433', 28),
('45678321098344', 'Saima Malik', '876544', 36),
('56789432109455', 'Kamran Ali', '987655', 31),
('67890543210566', 'Hina Khan', '098766', 37),
('78901654321677', 'Asad Butt', '109877', 29),
('89012765432788', 'Aisha Khan', '210988', 42),
('90123876543899', 'Khalid Ali', '321099', 34),
('01234987654900', 'Maria Malik', '432110', 39),
('12345098765021', 'Ahmed Khan', '543221', 33),
('23456109876132', 'Rabia Ahmed', '654332', 46),
('34567210987243', 'Ali Akhtar', '765443', 28),
('45678321098354', 'Sana Ali', '876554', 35),
('56789432109465', 'Umar Khan', '987665', 32),
('67890543210576', 'Fatima Malik', '098776', 38),
('78901654321687', 'Hassan Butt', '109887', 30),
('89012765432798', 'Amna Khan', '210998', 43),
('90123876543800', 'Saad Ahmed', '321009', 36),
('01234987654911', 'Ayesha Akram', '432120', 40),
('12345098765032', 'Bilal Malik', '543231', 34),
('23456109876143', 'Nadia Ali', '654342', 47),
('34567210987254', 'Hamza Khan', '765453', 29),
('45678321098365', 'Sanaullah Ali', '876564', 37),
('56789432109476', 'Maryam Butt', '987675', 31),
('67890543210587', 'Ahmed Malik', '098786', 38),
('78901654321698', 'Sadia Khan', '109897', 29),
('89012765432809', 'Zubair Ahmed', '210908', 41),
('90123876543810', 'Hina Ali', '321019', 35),
('01234987654921', 'Abdullah Khan', '432130', 48),
('12345098765043', 'Saba Ahmed', '543241', 26),
('23456109876154', 'Kamran Ali', '654352', 39),
('34567210987265', 'Ayesha Akhtar', '765463', 32),
('45678321098376', 'Amir Khan', '876574', 40),
('56789432109487', 'Aisha Malik', '987685', 33),
('67890543210598', 'Bilal Ali', '098796', 37);

-- Sample data for parties
INSERT INTO parties (name, logo, chairman) VALUES
('Pakistan Tehreek-e-Insaf', 'bat', '12345678901234'),
('Pakistan Muslim League-Nawaz', 'lion', '23456789012345'),
('Pakistan Peoples Party', 'arrow', '34567890123456'),
('Muttahida Qaumi Movement', 'kite', '45678901234567'),
('Jamiat Ulema-e-Islam (F)', 'book', '56789012345678'),
('Pakistan Muslim League-Quaid', 'sher', '67890123456789'),
('Pakistan Awami Tehreek', 'star', '78901234567890'),
('Muttahida Majlis-e-Amal Pakistan', 'scale', '89012345678901'),
('Awami National Party', 'lantern', '90123456789012'),
('Balochistan National Party (Mengal)', 'camel', '01234567890123');

-- Sample data for candidates
INSERT INTO candidates (cnic, party) VALUES
('12345678901234', 1),   -- Ali Khan (PTI)
('23456789012345', 2),   -- Sara Ahmed (PML-N)
('34567890123456', 3),   -- Ahmed Hassan (PPP)
('45678901234567', 4),   -- Fatima Khan (MQM)
('56789012345678', 5),   -- Usman Ali (JUI-F)
('67890123456789', 6),   -- Ayesha Mahmood (PML-Q)
('78901234567890', 7),   -- Ahmed Khan (PAT)
('89012345678901', 8),   -- Sanaullah Khan (MMA)
('90123456789012', 9),   -- Aisha Malik (ANP)
('01234567890123', 10),  -- Zubair Baloch (BNP)

('12345098765432', 1),   -- Fahad Ahmed (PTI)
('23456109876543', 2),   -- Sadia Khan (PML-N)
('34567210987654', 3),   -- Bilal Akhtar (PPP)
('45678321098765', 4),   -- Zainab Ali (MQM)
('56789432109876', 5),   -- Imran Ahmed (JUI-F)
('67890543210987', 6),   -- Maryam Khan (PML-Q)
('78901654321098', 7),   -- Khalid Malik (PAT)
('89012765432109', 8),   -- Nazia Butt (MMA)
('90123876543210', 9),   -- Naeem Khan (ANP)
('01234987654321', 10);  -- Amina Ahmed (BNP)

-- Sample data for admin
INSERT INTO admins
VALUES("100000", "umair", "123456"),
("200000", "daniyal", "123456"),
("300000", "bilal", "123456");

-- SELECT voters.cnic, voters.name, parties.logo, parties.name as party_name
-- FROM candidates
-- JOIN parties ON candidates.party = parties.id
-- JOIN voters ON candidates.cnic = voters.cnic;


-- INSERT INTO vote
-- VALUES ("34567890123456", "23456109876543", "2024-05-26 17:20:49");

-- select * from vote;

-- delete from vote;

--                     SELECT parties.name
--                     FROM parties
--                     JOIN candidates ON candidates.party = parties.id
--                     WHERE candidates.cnic = "56789432109876";