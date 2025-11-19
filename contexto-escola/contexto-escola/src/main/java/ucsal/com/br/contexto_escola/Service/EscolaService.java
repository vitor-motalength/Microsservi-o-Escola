package ucsal.com.br.contexto_escola.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucsal.com.br.contexto_escola.DTO.EscolaDTO;
import ucsal.com.br.contexto_escola.Entity.Escola;
import ucsal.com.br.contexto_escola.Exception.BadRequestException;
import ucsal.com.br.contexto_escola.Exception.ResourceNotFoundException;
import ucsal.com.br.contexto_escola.Repository.EscolaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EscolaService {

    private final EscolaRepository escolaRepository;

    public EscolaService(EscolaRepository escolaRepository) {
        this.escolaRepository = escolaRepository;
    }

    /**
     * Creates a new Escola.  Throws an exception if another school with the
     * same name already exists.
     */
    public EscolaDTO createEscola(EscolaDTO dto) {
        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new BadRequestException("Nome da escola é obrigatório");
        }
        if (escolaRepository.existsByNome(dto.nome())) {
            throw new BadRequestException("Já existe uma escola com o nome " + dto.nome());
        }
        Escola escola = new Escola(dto.nome());
        escolaRepository.save(escola);
        return toDto(escola);
    }

    public EscolaDTO getEscola(Long id) {
        Escola escola = escolaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Escola não encontrada com id " + id));
        return toDto(escola);
    }

    public List<EscolaDTO> listEscolas() {
        return escolaRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public EscolaDTO updateEscola(Long id, EscolaDTO dto) {
        Escola escola = escolaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Escola não encontrada com id " + id));
        if (dto.nome() != null && !dto.nome().trim().isEmpty()) {
            // check for uniqueness
            if (!escola.getNome().equals(dto.nome()) && escolaRepository.existsByNome(dto.nome())) {
                throw new BadRequestException("Já existe uma escola com o nome " + dto.nome());
            }
            escola.setNome(dto.nome());
        }
        escola.setAtivo(dto.ativo());
        return toDto(escolaRepository.save(escola));
    }

    public void deactivateEscola(Long id) {
        Escola escola = escolaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Escola não encontrada com id " + id));
        escola.setAtivo(false);
        escolaRepository.save(escola);
    }

    private EscolaDTO toDto(Escola escola) {
        return new EscolaDTO(escola.getId(), escola.getNome(), escola.isAtivo());
    }
}
