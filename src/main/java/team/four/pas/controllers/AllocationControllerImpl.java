package team.four.pas.controllers;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import team.four.pas.controllers.DTOs.AllocationAddDTO;
import team.four.pas.services.AllocationService;
import team.four.pas.services.data.allocations.VMAllocation;

import java.time.Instant;
import java.util.List;

@Path("/allocations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class AllocationControllerImpl {
    private final AllocationService allocationService;

    @GET
    public Response getAll() {
        return Response
                .status(Response.Status.OK)
                .entity(allocationService.getAll())
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        return Response
                .status(Response.Status.OK)
                .entity(allocationService.findById(id))
                .build();
    }

    @POST
    public Response createAllocation(@Valid AllocationAddDTO allocationAddDTO) {
        return Response
                .status(Response.Status.CREATED)
                .entity(allocationService.add(allocationAddDTO.clientId(), allocationAddDTO.resourceId(), Instant.now()))
                .build();
    }

    @GET
    @Path("/past/vm/{id}")
    public Response getPastVmAllocations(@PathParam("id") String id) {
        return Response
                .status(Response.Status.OK)
                .entity(allocationService.getPastVm(id))
                .build();
    }

    @GET
    @Path("/active/vm/{id}")
    public Response getActiveVmAllocations(@PathParam("id") String id) {
        return Response
                .status(Response.Status.OK)
                .entity(allocationService.getActiveVm(id))
                .build();
    }

    @GET
    @Path("/active/client/{id}")
    public Response getActiveClientAllocations(@PathParam("id") String id) {
        return Response
                .status(Response.Status.OK)
                .entity(allocationService.getActiveClient(id))
                .build();
    }

    @GET
    @Path("/past/client/{id}")
    public Response getPastClientAllocations(@PathParam("id") String id) {
        return Response
                .status(Response.Status.OK)
                .entity(allocationService.getPastClient(id))
                .build();
    }

    @PUT
    @Path("/{id}/finish")
    public Response finishAllocation(@PathParam("id") String id) {
        allocationService.finishAllocation(id);
        return Response
                .status(Response.Status.OK)
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAllocation(@PathParam("id") String id) {
        allocationService.delete(id);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}