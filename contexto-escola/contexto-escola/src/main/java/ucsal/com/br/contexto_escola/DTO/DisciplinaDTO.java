package ucsal.com.br.contexto_escola.DTO;

public record DisciplinaDTO(Long id,
                            String sigla,
                            String descricao,
                            Integer cargaHoraria,
                            Long cursoId,
                            String matriz,
                            boolean ativo
) {}
