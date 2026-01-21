-- Users (passwords are plain text for now - will need encoding after Spring Security is added)
INSERT INTO users (id, email, password, username, age, location, competencies)
VALUES (1, 'jan@example.com', 'password123', 'jan_de_bakker', 45, 'Amsterdam', 'Gardening, plumbing, general repairs');

INSERT INTO users (id, email, password, username, age, location, competencies)
VALUES (2, 'maria@example.com', 'password123', 'maria_visser', 32, 'Rotterdam', 'IT support, tax advice, languages');

INSERT INTO users (id, email, password, username, age, location, competencies)
VALUES (3, 'piet@example.com', 'password123', 'piet_jansen', 67, 'Utrecht', 'Company, transport, groceries');

INSERT INTO users (id, email, password, username, age, location, competencies)
VALUES (4, 'anna@example.com', 'password123', 'anna_smit', 28, 'Amsterdam', 'Pet sitting, painting, house chores');

INSERT INTO users (id, email, password, username, age, location, competencies)
VALUES (5, 'kees@example.com', 'password123', 'kees_de_vries', 55, 'Den Haag', 'Bureaucracy, moving, repairs');

-- Friendships (user_friends join table)
INSERT INTO user_friends (user_id, friend_id) VALUES (1, 2);
INSERT INTO user_friends (user_id, friend_id) VALUES (1, 3);
INSERT INTO user_friends (user_id, friend_id) VALUES (2, 1);
INSERT INTO user_friends (user_id, friend_id) VALUES (2, 4);
INSERT INTO user_friends (user_id, friend_id) VALUES (3, 1);
INSERT INTO user_friends (user_id, friend_id) VALUES (4, 2);
INSERT INTO user_friends (user_id, friend_id) VALUES (4, 5);
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

-- Activities
INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (6, 2, 'Weekly running group in the park', 'Rotterdam', '2025-01-14 08:00:00');
INSERT INTO activities (id, activity_type, event_date) VALUES (6, 'SPORTS', '2025-02-01 09:00:00');

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (7, 3, 'Visit to the Rijksmuseum together', 'Amsterdam', '2025-01-15 12:00:00');
INSERT INTO activities (id, activity_type, event_date) VALUES (7, 'CULTURE', '2025-02-10 13:00:00');

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (8, 5, 'Beach cleanup volunteering event', 'Den Haag', '2025-01-16 10:00:00');
INSERT INTO activities (id, activity_type, event_date) VALUES (8, 'VOLUNTEERING', '2025-02-15 10:00:00');

INSERT INTO posts (id, author_id, description, location, created_at)
VALUES (9, 4, 'Board game night at my place', 'Amsterdam', '2025-01-17 18:00:00');
INSERT INTO activities (id, activity_type, event_date) VALUES (9, 'SOCIAL', '2025-02-05 19:00:00');

-- Activity attendees (user_activities join table)
INSERT INTO user_activities (user_id, activity_id) VALUES (1, 6);
INSERT INTO user_activities (user_id, activity_id) VALUES (4, 6);
INSERT INTO user_activities (user_id, activity_id) VALUES (1, 7);
INSERT INTO user_activities (user_id, activity_id) VALUES (2, 7);
INSERT INTO user_activities (user_id, activity_id) VALUES (3, 8);
INSERT INTO user_activities (user_id, activity_id) VALUES (4, 8);
INSERT INTO user_activities (user_id, activity_id) VALUES (1, 9);
INSERT INTO user_activities (user_id, activity_id) VALUES (2, 9);
INSERT INTO user_activities (user_id, activity_id) VALUES (5, 9);

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

-- Messages
INSERT INTO messages (id, chat_id, sender_id, content, sent_at)
VALUES (1, 1, 1, 'Hi Maria, can you help me with my taxes next week?', '2025-01-18 10:00:00');
INSERT INTO messages (id, chat_id, sender_id, content, sent_at)
VALUES (2, 1, 2, 'Sure Jan! I have time on Thursday afternoon.', '2025-01-18 10:15:00');
INSERT INTO messages (id, chat_id, sender_id, content, sent_at)
VALUES (3, 1, 1, 'Perfect, see you then!', '2025-01-18 10:20:00');

INSERT INTO messages (id, chat_id, sender_id, content, sent_at)
VALUES (4, 2, 3, 'Jan, thanks for helping with my garden last week!', '2025-01-19 14:00:00');
INSERT INTO messages (id, chat_id, sender_id, content, sent_at)
VALUES (5, 2, 1, 'No problem Piet, happy to help a neighbor!', '2025-01-19 14:30:00');

INSERT INTO messages (id, chat_id, sender_id, content, sent_at)
VALUES (6, 3, 4, 'Kees, are you coming to the board game night?', '2025-01-20 09:00:00');
INSERT INTO messages (id, chat_id, sender_id, content, sent_at)
VALUES (7, 3, 5, 'Yes! I will bring Catan!', '2025-01-20 09:30:00');

-- Reset sequences to continue after our manual IDs
SELECT setval('users_id_seq', 5);
SELECT setval('posts_id_seq', 9);
SELECT setval('chats_id_seq', 3);
SELECT setval('messages_id_seq', 7);
