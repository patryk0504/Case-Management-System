package com.example.management.controller;

import com.example.management.model.Case;
import com.example.management.model.User;

public class RequestWrapper {
    private User _user;
    private Case _case;

    public void set_user(User _user) {
        this._user = _user;
    }

    public void set_case(Case _case) {
        this._case = _case;
    }

    public RequestWrapper(User _user, Case _case) {
        this._user = _user;
        this._case = _case;
    }

    public User get_user() {
        return _user;
    }

    public Case get_case() {
        return _case;
    }
}
