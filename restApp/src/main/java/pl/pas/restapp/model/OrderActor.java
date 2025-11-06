package pl.pas.restapp.model;

public sealed interface OrderActor permits Client, Restaurant, Worker {
    long getId();
}
