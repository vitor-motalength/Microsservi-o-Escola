package ucsal.com.br.contexto_escola.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucsal.com.br.contexto_escola.Entity.Curso;
import ucsal.com.br.contexto_escola.Entity.Escola;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    boolean existsByNome(String nome);
    List<Curso> findByEscola(Escola escola);
}
