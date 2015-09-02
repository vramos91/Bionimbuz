SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `seguranca` DEFAULT CHARACTER SET utf8 ;
USE `seguranca` ;

-- -----------------------------------------------------
-- Table `seguranca`.`arquivo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `seguranca`.`arquivo` ;

CREATE TABLE IF NOT EXISTS `seguranca`.`arquivo` (
  `idarquivo` INT NULL DEFAULT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idarquivo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `seguranca`.`atributo_arquivo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `seguranca`.`atributo_arquivo` ;

CREATE TABLE IF NOT EXISTS `seguranca`.`atributo_arquivo` (
  `idatributo_arquivo` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NULL DEFAULT NULL,
  `tipo` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idatributo_arquivo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `seguranca`.`arquivo_has_atributo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `seguranca`.`arquivo_has_atributo` ;

CREATE TABLE IF NOT EXISTS `seguranca`.`arquivo_has_atributo` (
  `arquivo_idarquivo` INT NOT NULL,
  `idatributo` INT NOT NULL,
  `valor` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`arquivo_idarquivo`, `idatributo`),
  INDEX `fk_arquivo_has_atributo_arquivo_atributo_arquivo1_idx` (`idatributo` ASC),
  INDEX `fk_arquivo_has_atributo_arquivo_arquivo_idx` (`arquivo_idarquivo` ASC),
  CONSTRAINT `fk_arquivo_has_atributo_arquivo_arquivo`
    FOREIGN KEY (`arquivo_idarquivo`)
    REFERENCES `seguranca`.`arquivo` (`idarquivo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_arquivo_has_atributo_arquivo_atributo_arquivo1`
    FOREIGN KEY (`idatributo`)
    REFERENCES `seguranca`.`atributo_arquivo` (`idatributo_arquivo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `seguranca`.`usuario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `seguranca`.`usuario` ;

CREATE TABLE IF NOT EXISTS `seguranca`.`usuario` (
  `idusuario` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `senha` VARCHAR(250) NOT NULL,
  PRIMARY KEY (`idusuario`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `seguranca`.`atributo_usuario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `seguranca`.`atributo_usuario` ;

CREATE TABLE IF NOT EXISTS `seguranca`.`atributo_usuario` (
  `idatributo` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NULL DEFAULT NULL,
  `tipo` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idatributo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `seguranca`.`usuario_has_atributo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `seguranca`.`usuario_has_atributo` ;

CREATE TABLE IF NOT EXISTS `seguranca`.`usuario_has_atributo` (
  `usuario_idusuario` INT NOT NULL,
  `atributo_idatributo` INT NOT NULL,
  `valor` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`usuario_idusuario`, `atributo_idatributo`),
  INDEX `fk_usuario_has_atributo_atributo1_idx` (`atributo_idatributo` ASC),
  INDEX `fk_usuario_has_atributo_usuario_idx` (`usuario_idusuario` ASC),
  CONSTRAINT `fk_usuario_has_atributo_usuario`
    FOREIGN KEY (`usuario_idusuario`)
    REFERENCES `seguranca`.`usuario` (`idusuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuario_has_atributo_atributo1`
    FOREIGN KEY (`atributo_idatributo`)
    REFERENCES `seguranca`.`atributo_usuario` (`idatributo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `seguranca`.`regras`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `seguranca`.`regras` ;

CREATE TABLE IF NOT EXISTS `seguranca`.`regras` (
  `idregra` INT NOT NULL AUTO_INCREMENT,
  `regra` VARCHAR(100) NULL,
  PRIMARY KEY (`idregra`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `seguranca`.`regrasql`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `seguranca`.`regrasql` ;

CREATE TABLE IF NOT EXISTS `seguranca`.`regrasql` (
  `idsql` INT NOT NULL AUTO_INCREMENT,
  `descricao` LONGTEXT NULL,
  `tipo` VARCHAR(45) NULL,
  `regra_idregra` INT NOT NULL,
  PRIMARY KEY (`idsql`),
  INDEX `fk_sql_regra1_idx` (`regra_idregra` ASC),
  CONSTRAINT `fk_sql_regra1`
    FOREIGN KEY (`regra_idregra`)
    REFERENCES `seguranca`.`regras` (`idregra`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `seguranca`.`regras_has_usuario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `seguranca`.`regras_has_usuario` ;

CREATE TABLE IF NOT EXISTS `seguranca`.`regras_has_usuario` (
  `regras_idregra` INT NOT NULL,
  `usuario_idusuario` INT NOT NULL,
  PRIMARY KEY (`regras_idregra`, `usuario_idusuario`),
  INDEX `fk_regras_has_usuario_usuario1_idx` (`usuario_idusuario` ASC),
  INDEX `fk_regras_has_usuario_regras1_idx` (`regras_idregra` ASC),
  CONSTRAINT `fk_regras_has_usuario_regras1`
    FOREIGN KEY (`regras_idregra`)
    REFERENCES `seguranca`.`regras` (`idregra`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_regras_has_usuario_usuario1`
    FOREIGN KEY (`usuario_idusuario`)
    REFERENCES `seguranca`.`usuario` (`idusuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `seguranca`.`regras_has_arquivo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `seguranca`.`regras_has_arquivo` ;

CREATE TABLE IF NOT EXISTS `seguranca`.`regras_has_arquivo` (
  `regras_idregra` INT NOT NULL,
  `arquivo_idarquivo` INT NOT NULL,
  PRIMARY KEY (`regras_idregra`, `arquivo_idarquivo`),
  INDEX `fk_regras_has_arquivo_arquivo1_idx` (`arquivo_idarquivo` ASC),
  INDEX `fk_regras_has_arquivo_regras1_idx` (`regras_idregra` ASC),
  CONSTRAINT `fk_regras_has_arquivo_regras1`
    FOREIGN KEY (`regras_idregra`)
    REFERENCES `seguranca`.`regras` (`idregra`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_regras_has_arquivo_arquivo1`
    FOREIGN KEY (`arquivo_idarquivo`)
    REFERENCES `seguranca`.`arquivo` (`idarquivo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `seguranca`.`arquivo_has_usuario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `seguranca`.`arquivo_has_usuario` ;

CREATE TABLE IF NOT EXISTS `seguranca`.`arquivo_has_usuario` (
  `arquivo_idarquivo` INT NOT NULL,
  `usuario_idusuario` INT NOT NULL,
  PRIMARY KEY (`arquivo_idarquivo`, `usuario_idusuario`),
  INDEX `fk_arquivo_has_usuario_usuario1_idx` (`usuario_idusuario` ASC),
  INDEX `fk_arquivo_has_usuario_arquivo1_idx` (`arquivo_idarquivo` ASC),
  CONSTRAINT `fk_arquivo_has_usuario_arquivo1`
    FOREIGN KEY (`arquivo_idarquivo`)
    REFERENCES `seguranca`.`arquivo` (`idarquivo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_arquivo_has_usuario_usuario1`
    FOREIGN KEY (`usuario_idusuario`)
    REFERENCES `seguranca`.`usuario` (`idusuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO atributo_arquivo (nome,tipo) VALUES ("tipo","arquivo");
INSERT INTO atributo_arquivo (nome,tipo) VALUES ("proprietario","arquivo");
INSERT INTO atributo_arquivo (nome,tipo) VALUES ("tamanho","arquivo");
INSERT INTO atributo_usuario (nome,tipo) VALUES ("permissao","usuario");
INSERT INTO regras (regra) VALUES ("a.tipo = aberto"); 
INSERT INTO regrasql (descricao,regra_idregra) VALUES ("SELECT u.nome, u.idarquivo FROM arquivo AS u,arquivo_has_atributo AS f, atributo_arquivo AS a WHERE u.idarquivo = f.arquivo_idarquivo AND a.idatributo_arquivo = f.idatributo AND a.nome = 'tipo' AND valor = 'aberto';",1);


