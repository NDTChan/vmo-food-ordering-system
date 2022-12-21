package com.food.ordering.system.dto.track;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record TrackOrderQuery(@NotNull UUID orderTrackingId) {
}