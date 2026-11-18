package com.universidad.seguridad.repository;

import com.universidad.seguridad.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByPropietarioId(Long usuarioId);

    List<Tarea> findByPropietarioEmail(String email);

    // SEGURO: @Query con parámetro nombrado. JPA lo envía como
    // PreparedStatement: el valor nunca se interpreta como JPQL/SQL.
    @Query("SELECT t FROM Tarea t WHERE t.titulo LIKE %:texto%")
    List<Tarea> buscarPorTitulo(@Param("texto") String texto);

    // Igual de seguro, pero limitado a las tareas de un usuario
    @Query("SELECT t FROM Tarea t WHERE t.propietario.email = :email AND t.titulo LIKE %:texto%")
    List<Tarea> buscarPorTituloDe(@Param("email") String email, @Param("texto") String texto);

    // INSEGURO - NUNCA HACER ESTO (solo referencia en comentario):
    // @Query(value = "SELECT * FROM tareas WHERE titulo LIKE '%" +
    //         texto + "%'", nativeQuery = true)
    // Si texto = "' OR '1'='1", la consulta devuelve TODAS las tareas
    // sin filtrar, ignorando por completo el criterio de búsqueda.
}
