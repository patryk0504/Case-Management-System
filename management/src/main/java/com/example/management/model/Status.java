package com.example.management.model;

public enum Status {
    NEW(1), IN_PROGRESS(2), DONE(3);
    private final int status;

    Status(int status){
        this.status = status;
    }

    public int getStatus(){
        return status;
    }
}
