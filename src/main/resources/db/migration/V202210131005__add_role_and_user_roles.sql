CREATE TABLE roles (
	id INTEGER NOT NULL AUTO_INCREMENT, 
	role varchar(255),
	PRIMARY KEY (id)
); 

 
CREATE TABLE users_roles (
	users_id INTEGER NOT NULL,
	roles_id INTEGER NOT NULL, 
	
	PRIMARY KEY (users_id, roles_id),
	FOREIGN KEY (users_id) REFERENCES users(id),
	FOREIGN KEY (roles_id) REFERENCES roles(id)
); 