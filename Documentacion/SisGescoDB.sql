--  SisGesco — Base de datos del Sistema Escolar
DROP DATABASE IF EXISTS sisgesco;
CREATE DATABASE IF NOT EXISTS sisgesco
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sisgesco;

-- 1. Tabla base para todos los usuarios del sistema
CREATE TABLE usuario (
  id            INT          AUTO_INCREMENT PRIMARY KEY,
  nombre        VARCHAR(100) NOT NULL,
  correo        VARCHAR(150) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  tipo          ENUM('alumno','profesor','administrador') NOT NULL,
  created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- 2. Alumno
CREATE TABLE alumno (
  matricula             VARCHAR(10) PRIMARY KEY,
  usuario_id            INT         NOT NULL UNIQUE,
  inscripcion_permitida BOOLEAN     NOT NULL DEFAULT FALSE,
  FOREIGN KEY (usuario_id) REFERENCES usuario(id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

-- 3. Profesor
CREATE TABLE profesor (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL UNIQUE,
  FOREIGN KEY (usuario_id) REFERENCES usuario(id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

-- 4. Administrador
CREATE TABLE administrador (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL UNIQUE,
  FOREIGN KEY (usuario_id) REFERENCES usuario(id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

-- 5. Materia
CREATE TABLE materia (
  clave  VARCHAR(10)  PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  dia    ENUM('Lunes','Martes','Miércoles',
              'Jueves','Viernes','Sábado') NULL,
  hora   VARCHAR(20) NULL
);

-- 6. Inscripción — N:M Alumno <-> Materia\
CREATE TABLE inscripcion (
  matricula         VARCHAR(10) NOT NULL,
  clave_materia     VARCHAR(10) NOT NULL,
  fecha_inscripcion DATE        NOT NULL DEFAULT (CURRENT_DATE),
  PRIMARY KEY (matricula, clave_materia),
  FOREIGN KEY (matricula)     REFERENCES alumno(matricula)  ON DELETE CASCADE,
  FOREIGN KEY (clave_materia) REFERENCES materia(clave)     ON DELETE CASCADE
);

-- 7. Relación Profesor <-> Materia
CREATE TABLE profesor_materia (
  profesor_id   INT         NOT NULL,
  clave_materia VARCHAR(10) NOT NULL,
  PRIMARY KEY (profesor_id, clave_materia),
  FOREIGN KEY (profesor_id)   REFERENCES profesor(id)    ON DELETE CASCADE,
  FOREIGN KEY (clave_materia) REFERENCES materia(clave)  ON DELETE CASCADE
);

-- 8. Calificación — escala 0 a 100
CREATE TABLE calificacion (
  id             INT          AUTO_INCREMENT PRIMARY KEY,
  matricula      VARCHAR(10)  NOT NULL,
  clave_materia  VARCHAR(10)  NOT NULL,
  valor          DECIMAL(5,2) NOT NULL
                   CHECK (valor BETWEEN 0 AND 100),   
  fecha_registro DATE         NOT NULL DEFAULT (CURRENT_DATE),
  UNIQUE  (matricula, clave_materia),
  INDEX   idx_matricula (matricula),
  FOREIGN KEY (matricula)     REFERENCES alumno(matricula) ON DELETE CASCADE,
  FOREIGN KEY (clave_materia) REFERENCES materia(clave)    ON DELETE CASCADE
);

-- 9. Vista Kardex
CREATE OR REPLACE VIEW v_kardex AS
  SELECT
    a.matricula,
    u.nombre,
    m.clave      AS clave_materia,
    m.nombre     AS nombre_materia,
    c.valor,
    c.fecha_registro,
    ROUND(AVG(c.valor) OVER (PARTITION BY a.matricula), 2) AS promedio_general
  FROM calificacion c
  JOIN alumno  a ON c.matricula     = a.matricula
  JOIN usuario u ON a.usuario_id    = u.id
  JOIN materia m ON c.clave_materia = m.clave;

--  DATOS DE PRUEBA

-- Administrador inicial 
INSERT INTO usuario (nombre, correo, password_hash, tipo) VALUES
  ('Administrador', 'admin@sisgesco.com', 'C34dZ23,0407', 'administrador');
INSERT INTO administrador (usuario_id) VALUES (LAST_INSERT_ID());

-- Alumno de prueba 
INSERT INTO usuario (nombre, correo, password_hash, tipo) VALUES
  ('Marco Ruiz', 'maruiz@itz.com', 'C34dZ23.0407', 'alumno');
INSERT INTO alumno (matricula, usuario_id, inscripcion_permitida) VALUES
  ('A001', LAST_INSERT_ID(), TRUE);

-- Materia de prueba
INSERT INTO materia (clave, nombre, dia, hora) VALUES
  ('MFP1', 'Fundamentos de Programación', 'Lunes', '08:00');

-- Inscripción y calificación de prueba
INSERT INTO inscripcion (matricula, clave_materia) VALUES
  ('A001', 'MFP1');

INSERT INTO calificacion (matricula, clave_materia, valor) VALUES
  ('A001', 'MFP1', 85.0);   

-- Verificación rápida
SELECT 'Usuarios creados:' AS info, COUNT(*) AS total FROM usuario
UNION ALL
SELECT 'Administradores:', COUNT(*) FROM administrador
UNION ALL
SELECT 'Alumnos:',         COUNT(*) FROM alumno
UNION ALL
SELECT 'Materias:',        COUNT(*) FROM materia;

SELECT * FROM v_kardex;