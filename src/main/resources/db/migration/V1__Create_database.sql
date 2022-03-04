
CREATE TABLE IF NOT EXISTS `student_db`.`student` (
    `student_id` INT NOT NULL AUTO_INCREMENT,
    `first_name` VARCHAR(20) NOT NULL,
    `last_name` VARCHAR(30) NOT NULL,
    `mail` VARCHAR(50) NULL,
    `index_number` INT NOT NULL,
    `birth_day` DATE NOT NULL,
    PRIMARY KEY (`student_id`),
    UNIQUE INDEX `indexNumber_UNIQUE` (`index_number` ASC) VISIBLE);

CREATE TABLE IF NOT EXISTS `student_db`.`course` (
    `course_id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    `description` VARCHAR(200) NOT NULL,
    PRIMARY KEY (`course_id`));


CREATE TABLE IF NOT EXISTS `student_db`.`student_course` (
    `student_id` INT NOT NULL,
    `course_id` INT NOT NULL,
    PRIMARY KEY (`student_id`, `course_id`),
    INDEX `fk_student_has_course_course1_idx` (`course_id` ASC) VISIBLE,
    INDEX `fk_student_has_course_student1_idx` (`student_id` ASC) VISIBLE,
    CONSTRAINT `fk_student_has_course_student1`
    FOREIGN KEY (`student_id`)
    REFERENCES `student_db`.`student` (`student_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_student_has_course_course1`
    FOREIGN KEY (`course_id`)
    REFERENCES `student_db`.`course` (`course_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);