INSERT INTO users VALUES ( default, 'Denny', 'Cooper', 'denny.cooper@gmail.com', '$2a$10$2f6TA44Cj/BRSdvLG5sZtu3PLHr3hix2bjWjoIHK00IAqT3RgSrj.', '880231243', TRUE ); --password: 01234567
INSERT INTO users VALUES ( default, 'Romeo', 'Pitt', 'rpittt@gmail.com', '$2a$10$VhJU8Pte48wjo7Ns.I6fgOK837Pbho0TO9eD5ciTWBFCUHdiXdUsO', '7364829046', FALSE ); --password: password
INSERT INTO users VALUES ( default, 'Katty', 'Spielberg', 'katesp2000@gmail.com', '$2a$10$VhJU8Pte48wjo7Ns.I6fgOK837Pbho0TO9eD5ciTWBFCUHdiXdUsO', '267392922', FALSE ); --password: password
INSERT INTO users VALUES ( default, 'Alex', 'Ferdinand', 'alex.ferd04@gmail.com', '$2a$10$X6.32grSsgf6E13zOP91iuVauA/yZAGh9zm76ftVbgCOJD00m4yJi', '4728829100', FALSE ); --password: 12345678

INSERT INTO questions VALUES ( default, 'Do you like it?', 'SINGLE_LINE', 1, 2 );
INSERT INTO questions VALUES ( default, 'When were you born?', 'SINGLE_LINE', 3, 4 );
INSERT INTO questions VALUES ( default, 'What is your name?', 'SINGLE_LINE', 2, 1 );
INSERT INTO questions VALUES ( default, 'Are you ok?', 'COMBOBOX', 1, 3 );

INSERT INTO answers VALUES ( default, 'Yes, I do.', 1 );
INSERT INTO answers VALUES ( default, '23 Jun 1992', 2 );
INSERT INTO answers VALUES ( default, 'Denny', 3 );

INSERT INTO answer_options VALUES ( default, 'yes', 4 );
INSERT INTO answer_options VALUES ( default, 'no', 4 );
INSERT INTO answer_options VALUES ( default, 'idk', 4 );