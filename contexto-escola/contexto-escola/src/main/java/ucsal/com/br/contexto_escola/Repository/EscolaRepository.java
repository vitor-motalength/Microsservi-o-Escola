package ucsal.com.br.contexto_escola.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucsal.com.br.contexto_escola.Entity.Escola;

@Repository
public interface EscolaRepository extends JpaRepository<Escola, Long> {
    boolean existsByNome(String nome);
}
