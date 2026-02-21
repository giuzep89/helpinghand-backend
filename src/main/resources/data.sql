-- Authorities
INSERT INTO authorities (authority) VALUES ('ROLE_USER');
INSERT INTO authorities (authority) VALUES ('ROLE_ADMIN');

-- Users (passwords are BCrypt encoded - plain text was "password123")
-- BCrypt hash: $2a$12$SOP2Me9pQyLwaRp9406htOsP/2tk37shKTPQSeWLbKQBqR35/0bAS
INSERT INTO users (id, email, password, username, age, location, competencies, enabled)
VALUES (1, 'jan@example.com', '$2a$12$SOP2Me9pQyLwaRp9406htOsP/2tk37shKTPQSeWLbKQBqR35/0bAS', 'jan_de_bakker', 45, 'Amsterdam', 'Gardening, plumbing, general repairs', true);

INSERT INTO users (id, email, password, username, age, location, competencies, enabled)
VALUES (2, 'maria@example.com', '$2a$12$SOP2Me9pQyLwaRp9406htOsP/2tk37shKTPQSeWLbKQBqR35/0bAS', 'maria_visser', 32, 'Rotterdam', 'IT support, tax advice, languages', true);

INSERT INTO users (id, email, password, username, age, location, competencies, enabled)
VALUES (3, 'piet@example.com', '$2a$12$SOP2Me9pQyLwaRp9406htOsP/2tk37shKTPQSeWLbKQBqR35/0bAS', 'piet_jansen', 67, 'Utrecht', 'Company, transport, groceries', true);

INSERT INTO users (id, email, password, username, age, location, competencies, enabled)
VALUES (4, 'anna@example.com', '$2a$12$SOP2Me9pQyLwaRp9406htOsP/2tk37shKTPQSeWLbKQBqR35/0bAS', 'anna_smit', 28, 'Amsterdam', 'Pet sitting, painting, house chores', true);

INSERT INTO users (id, email, password, username, age, location, competencies, enabled)
VALUES (5, 'kees@example.com', '$2a$12$SOP2Me9pQyLwaRp9406htOsP/2tk37shKTPQSeWLbKQBqR35/0bAS', 'kees_de_vries', 55, 'Den Haag', 'Bureaucracy, moving, repairs', true);

-- User authorities (all users get ROLE_USER, jan_de_bakker also gets ROLE_ADMIN)
INSERT INTO user_authorities (user_id, authority) VALUES (1, 'ROLE_USER');
INSERT INTO user_authorities (user_id, authority) VALUES (1, 'ROLE_ADMIN');
INSERT INTO user_authorities (user_id, authority) VALUES (2, 'ROLE_USER');
INSERT INTO user_authorities (user_id, authority) VALUES (3, 'ROLE_USER');
INSERT INTO user_authorities (user_id, authority) VALUES (4, 'ROLE_USER');
INSERT INTO user_authorities (user_id, authority) VALUES (5, 'ROLE_USER');

-- Friendships (user_friends join table)
INSERT INTO user_friends (user_id, friend_id) VALUES (1, 2);
INSERT INTO user_friends (user_id, friend_id) VALUES (1, 3);
INSERT INTO user_friends (user_id, friend_id) VALUES (1, 4);
INSERT INTO user_friends (user_id, friend_id) VALUES (2, 1);
INSERT INTO user_friends (user_id, friend_id) VALUES (2, 3);
INSERT INTO user_friends (user_id, friend_id) VALUES (2, 4);
INSERT INTO user_friends (user_id, friend_id) VALUES (3, 1);
INSERT INTO user_friends (user_id, friend_id) VALUES (3, 2);
INSERT INTO user_friends (user_id, friend_id) VALUES (3, 5);
INSERT INTO user_friends (user_id, friend_id) VALUES (4, 1);
INSERT INTO user_friends (user_id, friend_id) VALUES (4, 2);
INSERT INTO user_friends (user_id, friend_id) VALUES (4, 5);
INSERT INTO user_friends (user_id, friend_id) VALUES (5, 3);
INSERT INTO user_friends (user_id, friend_id) VALUES (5, 4);

-- Posts (base table for HelpRequests and Activities - JOINED inheritance)
-- HelpRequests
INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (1, 1, 'Need help with my garden, lots of weeds to remove', 'Amsterdam', '2025-01-15 10:00:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (1, 'GARDENING', false);

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (2, 2, 'Looking for someone to help with my tax return', 'Rotterdam', '2025-01-16 14:30:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (2, 'TAXES', false);

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (3, 3, 'My computer keeps crashing, need IT help', 'Utrecht', '2025-01-17 09:15:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (3, 'IT', false);

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (4, 4, 'Need help moving to a new apartment next week', 'Amsterdam', '2025-01-18 11:00:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (4, 'MOVING', false);

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (5, 1, 'Sink is leaking, need a plumber', 'Amsterdam', '2025-01-19 16:45:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (5, 'PLUMBING', true);

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (6, 2, 'Need someone to walk my dog while I am at work', 'Rotterdam', '2025-01-20 08:00:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (6, 'PETSITTING', false);

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (7, 5, 'Looking for help translating a letter from German', 'Den Haag', '2025-01-20 11:30:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (7, 'LANGUAGE', false);

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (8, 1, 'Can someone help me paint my living room?', 'Amsterdam', '2025-01-21 09:00:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (8, 'PAINTING', false);

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (9, 3, 'Need help picking up groceries, I cannot drive', 'Utrecht', '2025-01-21 14:00:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (9, 'GROCERIES', false);

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (10, 4, 'Looking for someone to keep my grandmother company', 'Amsterdam', '2025-01-22 10:00:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (10, 'COMPANY', false);

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (11, 2, 'Need help assembling IKEA furniture', 'Rotterdam', '2025-01-22 16:00:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (11, 'HOUSE_CHORES', false);

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (12, 5, 'Can someone drive me to the airport next Monday?', 'Den Haag', '2025-01-23 07:00:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (12, 'TRANSPORT', false);

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (13, 1, 'Broken window frame needs repair', 'Amsterdam', '2025-01-23 13:00:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (13, 'REPAIRS', false);

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (14, 3, 'Help needed filling out government forms', 'Utrecht', '2025-01-24 09:30:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (14, 'BUREAUCRACY', false);

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (15, 4, 'Starting a small business, need advice', 'Amsterdam', '2025-01-24 15:00:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (15, 'COMPANY', false);

-- Activities
INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (16, 2, 'Weekly running group in the park', 'Rotterdam', '2025-01-14 08:00:00');
INSERT INTO activities (id, activity_type, event_date) VALUES (16, 'SPORTS', '2025-02-01 09:00:00');

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (17, 3, 'Visit to the Rijksmuseum together', 'Amsterdam', '2025-01-15 12:00:00');
INSERT INTO activities (id, activity_type, event_date) VALUES (17, 'CULTURE', '2025-02-10 13:00:00');

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (18, 5, 'Beach cleanup volunteering event', 'Den Haag', '2025-01-16 10:00:00');
INSERT INTO activities (id, activity_type, event_date) VALUES (18, 'VOLUNTEERING', '2025-02-15 10:00:00');

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (19, 4, 'Board game night at my place', 'Amsterdam', '2025-01-17 18:00:00');
INSERT INTO activities (id, activity_type, event_date) VALUES (19, 'SOCIAL', '2025-02-05 19:00:00');

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (20, 1, 'Cycling tour through the countryside', 'Amsterdam', '2025-01-25 08:00:00');
INSERT INTO activities (id, activity_type, event_date) VALUES (20, 'SPORTS', '2025-02-20 09:00:00');

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (21, 5, 'Free Dutch language practice session', 'Den Haag', '2025-01-25 12:00:00');
INSERT INTO activities (id, activity_type, event_date) VALUES (21, 'EDUCATIONAL', '2025-02-22 14:00:00');

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (22, 2, 'Neighborhood cleanup event', 'Rotterdam', '2025-01-26 10:00:00');
INSERT INTO activities (id, activity_type, event_date) VALUES (22, 'VOLUNTEERING', '2025-02-25 10:00:00');

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (23, 3, 'Coffee and chat meetup for seniors', 'Utrecht', '2025-01-26 14:00:00');
INSERT INTO activities (id, activity_type, event_date) VALUES (23, 'SOCIAL', '2025-02-28 15:00:00');

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (24, 4, 'Visit to Van Gogh Museum', 'Amsterdam', '2025-01-27 11:00:00');
INSERT INTO activities (id, activity_type, event_date) VALUES (24, 'CULTURE', '2025-03-01 13:00:00');

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (25, 1, 'Looking for someone to help fix my bicycle', 'Amsterdam', '2025-01-28 09:00:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (25, 'REPAIRS', false);

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (26, 2, 'Need help setting up a new printer', 'Rotterdam', '2025-01-28 14:00:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (26, 'IT', false);

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (27, 3, 'Yoga in the park session', 'Utrecht', '2025-01-29 08:00:00');
INSERT INTO activities (id, activity_type, event_date) VALUES (27, 'SPORTS', '2025-03-05 10:00:00');

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (28, 4, 'Can someone water my plants while I am on holiday?', 'Amsterdam', '2025-01-29 16:00:00');
INSERT INTO help_requests (id, help_type, help_found) VALUES (28, 'GARDENING', false);

-- Activity attendees (user_activities join table)
INSERT INTO user_activities (user_id, activity_id) VALUES (1, 16);
INSERT INTO user_activities (user_id, activity_id) VALUES (4, 16);
INSERT INTO user_activities (user_id, activity_id) VALUES (1, 17);
INSERT INTO user_activities (user_id, activity_id) VALUES (2, 17);
INSERT INTO user_activities (user_id, activity_id) VALUES (3, 18);
INSERT INTO user_activities (user_id, activity_id) VALUES (4, 18);
INSERT INTO user_activities (user_id, activity_id) VALUES (1, 19);
INSERT INTO user_activities (user_id, activity_id) VALUES (2, 19);
INSERT INTO user_activities (user_id, activity_id) VALUES (5, 19);
INSERT INTO user_activities (user_id, activity_id) VALUES (2, 20);
INSERT INTO user_activities (user_id, activity_id) VALUES (3, 20);
INSERT INTO user_activities (user_id, activity_id) VALUES (1, 21);
INSERT INTO user_activities (user_id, activity_id) VALUES (4, 21);
INSERT INTO user_activities (user_id, activity_id) VALUES (1, 22);
INSERT INTO user_activities (user_id, activity_id) VALUES (3, 22);
INSERT INTO user_activities (user_id, activity_id) VALUES (5, 22);
INSERT INTO user_activities (user_id, activity_id) VALUES (1, 23);
INSERT INTO user_activities (user_id, activity_id) VALUES (2, 23);
INSERT INTO user_activities (user_id, activity_id) VALUES (2, 24);
INSERT INTO user_activities (user_id, activity_id) VALUES (5, 24);

-- User prizes (from completed help requests - can accumulate multiple of same type)
-- Maria helped with plumbing and IT multiple times
INSERT INTO user_prizes (user_id, prizes) VALUES (2, 'PLUMBING');
INSERT INTO user_prizes (user_id, prizes) VALUES (2, 'IT');
INSERT INTO user_prizes (user_id, prizes) VALUES (2, 'IT');
INSERT INTO user_prizes (user_id, prizes) VALUES (2, 'TAXES');

-- Jan is a gardening and repairs expert
INSERT INTO user_prizes (user_id, prizes) VALUES (1, 'GARDENING');
INSERT INTO user_prizes (user_id, prizes) VALUES (1, 'GARDENING');
INSERT INTO user_prizes (user_id, prizes) VALUES (1, 'GARDENING');
INSERT INTO user_prizes (user_id, prizes) VALUES (1, 'REPAIRS');
INSERT INTO user_prizes (user_id, prizes) VALUES (1, 'PLUMBING');

-- Piet loves keeping company and helping with transport
INSERT INTO user_prizes (user_id, prizes) VALUES (3, 'COMPANY');
INSERT INTO user_prizes (user_id, prizes) VALUES (3, 'COMPANY');
INSERT INTO user_prizes (user_id, prizes) VALUES (3, 'TRANSPORT');
INSERT INTO user_prizes (user_id, prizes) VALUES (3, 'GROCERIES');

-- Anna helps with pets and painting
INSERT INTO user_prizes (user_id, prizes) VALUES (4, 'PETSITTING');
INSERT INTO user_prizes (user_id, prizes) VALUES (4, 'PETSITTING');
INSERT INTO user_prizes (user_id, prizes) VALUES (4, 'PAINTING');
INSERT INTO user_prizes (user_id, prizes) VALUES (4, 'HOUSE_CHORES');

-- Kees is the bureaucracy and moving guy
INSERT INTO user_prizes (user_id, prizes) VALUES (5, 'BUREAUCRACY');
INSERT INTO user_prizes (user_id, prizes) VALUES (5, 'BUREAUCRACY');
INSERT INTO user_prizes (user_id, prizes) VALUES (5, 'MOVING');
INSERT INTO user_prizes (user_id, prizes) VALUES (5, 'MOVING');
INSERT INTO user_prizes (user_id, prizes) VALUES (5, 'LANGUAGE');

-- Chats
INSERT INTO chats (id, user_one_id, user_two_id) VALUES (1, 1, 2);
INSERT INTO chats (id, user_one_id, user_two_id) VALUES (2, 1, 3);
INSERT INTO chats (id, user_one_id, user_two_id) VALUES (3, 4, 5);
INSERT INTO chats (id, user_one_id, user_two_id) VALUES (4, 2, 3);
INSERT INTO chats (id, user_one_id, user_two_id) VALUES (5, 3, 5);
INSERT INTO chats (id, user_one_id, user_two_id) VALUES (6, 1, 4);

-- Messages
INSERT INTO messages (id, chat_id, sender_id, content, timestamp)
VALUES (1, 1, 1, 'Hi Maria, can you help me with my taxes next week?', '2025-01-18 10:00:00');
INSERT INTO messages (id, chat_id, sender_id, content, timestamp)
VALUES (2, 1, 2, 'Sure Jan! I have time on Thursday afternoon.', '2025-01-18 10:15:00');
INSERT INTO messages (id, chat_id, sender_id, content, timestamp)
VALUES (3, 1, 1, 'Perfect, see you then!', '2025-01-18 10:20:00');

INSERT INTO messages (id, chat_id, sender_id, content, timestamp)
VALUES (4, 2, 3, 'Jan, thanks for helping with my garden last week!', '2025-01-19 14:00:00');
INSERT INTO messages (id, chat_id, sender_id, content, timestamp)
VALUES (5, 2, 1, 'No problem Piet, happy to help a neighbor!', '2025-01-19 14:30:00');

INSERT INTO messages (id, chat_id, sender_id, content, timestamp)
VALUES (6, 3, 4, 'Kees, are you coming to the board game night?', '2025-01-20 09:00:00');
INSERT INTO messages (id, chat_id, sender_id, content, timestamp)
VALUES (7, 3, 5, 'Yes! I will bring Catan!', '2025-01-20 09:30:00');

INSERT INTO messages (id, chat_id, sender_id, content, timestamp)
VALUES (8, 4, 2, 'Hey Piet, how is the museum visit coming along?', '2025-01-21 11:00:00');
INSERT INTO messages (id, chat_id, sender_id, content, timestamp)
VALUES (9, 4, 3, 'Great! We have 4 people signed up already.', '2025-01-21 11:15:00');

INSERT INTO messages (id, chat_id, sender_id, content, timestamp)
VALUES (10, 5, 3, 'Kees, can you help me with some bureaucracy stuff?', '2025-01-22 09:00:00');
INSERT INTO messages (id, chat_id, sender_id, content, timestamp)
VALUES (11, 5, 5, 'Of course! What do you need help with?', '2025-01-22 09:20:00');
INSERT INTO messages (id, chat_id, sender_id, content, timestamp)
VALUES (12, 5, 3, 'I need to fill out some forms for the municipality.', '2025-01-22 09:25:00');

INSERT INTO messages (id, chat_id, sender_id, content, timestamp)
VALUES (13, 6, 1, 'Hi Anna, thanks for offering to help with the painting!', '2025-01-23 14:00:00');
INSERT INTO messages (id, chat_id, sender_id, content, timestamp)
VALUES (14, 6, 4, 'No problem Jan! What color are you thinking?', '2025-01-23 14:10:00');
INSERT INTO messages (id, chat_id, sender_id, content, timestamp)
VALUES (15, 6, 1, 'I was thinking a light blue for the living room.', '2025-01-23 14:15:00');

-- Reset sequences to continue after our manual IDs
SELECT setval('users_id_seq', 5);
SELECT setval('posts_id_seq', 28);
SELECT setval('chats_id_seq', 6);
SELECT setval('messages_id_seq', 15);
