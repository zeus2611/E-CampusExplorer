package com.example.firsttrial

class User(
    var userId: String = "",
    var name: String = "",
    var email: String = "",
    var password: String = ""
) {
    constructor(name: String, email: String, password: String) : this() {
        this.name = name
        this.email = email
        this.password = password
    }
}
