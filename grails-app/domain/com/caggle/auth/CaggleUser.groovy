package com.caggle.auth

class CaggleUser {

    private static final long serialVersionUID = 1

    transient passwordEncoder

    String username
    String email
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired


    CaggleUser(String username, String password) {
        this()
        this.username = username
        this.password = password
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = password.encodeAsSHA1()
//        password = passwordEncoder ? passwordEncoder.encodePassword(password,null) : password
    }

//    static transients = ['springSecurityService']

    static constraints = {
        username blank: false
        email blank: false, unique: true
        password blank: false
    }

    static mapping = {
        password column: '`password`'
    }
}
