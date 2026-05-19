package sn.thiordev221.app.service.implementations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import sn.thiordev221.app.dto.responses.PartageResponse;
import sn.thiordev221.app.service.contrats.PartageService;

public class PartageServiceImpl implements PartageService{
    
    @Override
    public PartageResponse inviterUtilisateur(Long listId, Long inviteId, Long currentUserId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void revoquerPartage(Long listId, Long inviteId, Long currentUserId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Page<PartageResponse> getPartagesDeMaListe(Long listId, Long currentUserId, Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
