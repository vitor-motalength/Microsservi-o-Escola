package ucsal.com.br.contexto_escola.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucsal.com.br.contexto_escola.Entity.Curso;
import ucsal.com.br.contexto_escola.Entity.Disciplina;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    boolean existsBySigla(String sigla);
    Optional<Disciplina> findBySigla(String sigla);
    List<Disciplina> findByCurso(Curso curso);
}
