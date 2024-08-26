package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.core.exception.InvalidPeriodException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class StatisticsValidator {

    public void validatePeriod(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new InvalidPeriodException();
        }

        long daysBetween = ChronoUnit.DAYS.between(start, end);
        if (daysBetween > 6) {
            throw new InvalidPeriodException();
        }
    }
}
