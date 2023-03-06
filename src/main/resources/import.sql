insert into users (name, email) values ('lemur', 'lemur@gmail.com');
insert into users (name, email) values ('danilo', 'danilo@gmail.com');
insert into users (name, email) values ('govece', 'govece@gmail.com');

insert into messages (subject, message, is_deleted_for_reciver, is_deleted_for_sender, sender_id, reciver_id, date) values ('lemur->danilo', 'poruka od lemura za danila', false, false, 1, 2, NOW());
insert into messages (subject, message, is_deleted_for_reciver, is_deleted_for_sender, sender_id, reciver_id, date) values ('danilo->lemur', 'poruka od danila za lemura', false, false, 2, 1, NOW());
insert into messages (subject, message, is_deleted_for_reciver, is_deleted_for_sender, sender_id, reciver_id, date) values ('lemur->govece', 'poruka od lemura za govece', false, false, 1, 3, NOW());

