CREATE TABLE tasks
(
    task_id SERIAL NOT NULL,
    task TEXT NOT NULL,
    date DATE NOT NUll,
    is_done BOOLEAN NOT NULL,
    PRIMARY KEY (task_id)
);

CREATE TABLE allowed_requests(
    allowed_request_id SERIAL,
    service_from TEXT,
    service_to TEXT,
    PRIMARY KEY (allowed_request_id),
    UNIQUE (service_from, service_to)
);

CREATE TABLE tokens(
    token_id SERIAL,
    service_from TEXT,
    service_to TEXT,
    sending_time bigint,
    code double precision,
    PRIMARY KEY (token_id)
);

INSERT INTO allowed_requests(service_from, service_to) VALUES ('gateway-service', 'task-service');
INSERT INTO allowed_requests(service_from, service_to) VALUES ('gateway-service', 'notification-service');

INSERT INTO tasks(task, date, is_done) VALUES ('cook a diner', '2021-08-06', false);
INSERT INTO tasks(task, date, is_done) VALUES ('prepare for test', '2021-08-06', false);
INSERT INTO tasks(task, date, is_done) VALUES ('fix bugs in program', '2021-08-06', false);
INSERT INTO tasks(task, date, is_done) VALUES ('meet with Ivan', '2021-08-06', false);
INSERT INTO tasks(task, date, is_done) VALUES ('check marks', '2021-08-06', false);
