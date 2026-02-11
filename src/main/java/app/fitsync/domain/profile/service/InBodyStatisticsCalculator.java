package app.fitsync.domain.profile.service;

import app.fitsync.domain.profile.dto.InBodyStatisticsResponse.InBodySummary;
import app.fitsync.domain.profile.entity.InBodyRecord;
import java.util.List;

public interface InBodyStatisticsCalculator {
    InBodySummary calculateSummary(List<InBodyRecord> records);
}
