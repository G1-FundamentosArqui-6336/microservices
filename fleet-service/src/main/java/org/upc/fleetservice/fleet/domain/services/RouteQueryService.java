package org.upc.fleetservice.fleet.domain.services;



import org.upc.fleetservice.fleet.domain.model.aggregates.Route;
import org.upc.fleetservice.fleet.domain.model.queries.GetAllRoutesByDriverId;
import org.upc.fleetservice.fleet.domain.model.queries.GetAllRoutesQuery;
import org.upc.fleetservice.fleet.domain.model.queries.GetRouteByIdQuery;

import java.util.List;
import java.util.Optional;

public interface RouteQueryService {

    List<Route> handle(GetAllRoutesQuery query);
    Optional<Route> handle(GetRouteByIdQuery query);
    List<Route> handle(GetAllRoutesByDriverId query);
}
