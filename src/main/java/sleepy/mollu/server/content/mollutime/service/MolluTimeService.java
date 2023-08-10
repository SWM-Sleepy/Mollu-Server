package sleepy.mollu.server.content.mollutime.service;

import sleepy.mollu.server.content.mollutime.controller.dto.SearchMolluTimeResponse;

public interface MolluTimeService {

    SearchMolluTimeResponse searchMolluTime(String memberId);
}
