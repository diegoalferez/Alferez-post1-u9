-- Admin (clave: admin123, hash BCrypt de costo 12) y 2 tareas de ejemplo
INSERT IGNORE INTO usuarios (nombre, email, contrasenia, rol, activo)
VALUES ('Administrador', 'admin@universidad.edu', '$2a$12$SF6dOKPUTkBqe6IzN541yeAaXqsEub1lJw5D5hI4TyJPRPJF7HGqm', 'ROLE_ADMIN', 1);

INSERT INTO tareas (titulo, descripcion, usuario_id, completada)
SELECT 'Revisar seguridad', 'Tarea de ejemplo del admin', id, 0 FROM usuarios WHERE email = 'admin@universidad.edu';
INSERT INTO tareas (titulo, descripcion, usuario_id, completada)
SELECT 'Preparar informe', 'Segunda tarea de ejemplo', id, 0 FROM usuarios WHERE email = 'admin@universidad.edu';
