CREATE SCHEMA IF NOT EXISTS student_db;
CREATE TABLE IF NOT EXISTS `student` (
`student_id` INT NOT NULL AUTO_INCREMENT,
`first_name` VARCHAR(20) NOT NULL,
`last_name` VARCHAR(30) NOT NULL,
`mail` VARCHAR(50) NULL,
`index_number` INT NOT NULL,
`birth_day` DATE NOT NULL,
PRIMARY KEY (`student_id`));

CREATE TABLE IF NOT EXISTS `course` (
`course_id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(50) NOT NULL,
`description` VARCHAR(200) NOT NULL);



CREATE TABLE IF NOT EXISTS `student_course` (
`student_id` INT NOT NULL,
`course_id` INT NOT NULL,
PRIMARY KEY (`student_id`, `course_id`),
CONSTRAINT `fk_student_has_course_student1`
FOREIGN KEY (`student_id`)
REFERENCES `student` (`student_id`),
CONSTRAINT `fk_student_has_course_course1`
FOREIGN KEY (`course_id`)
REFERENCES `course` (`course_id`));