package com.caggle.auth

import grails.converters.JSON
import org.hibernate.criterion.CriteriaSpecification
import org.hibernate.transform.ResultTransformer

import java.sql.SQLException

class UserController {

    def signup() {
        def params = request.JSON
        println "params...." + params
        def flag = false
        try {
            def caggleUser = new CaggleUser(username: params.user.name, password: params.user.password, email: params.user.email)
            flag = caggleUser.save(flush: true)
        } catch (SQLException e ) {
            e.printStackTrace()
        }

        def response = [:]
        if(flag) {
            response.code = 200
            response.message = "Success"
        } else {
            response.code = 500
            response.message = "Error"
        }

        render response as JSON
    }

    def login() {
        def params = request.JSON
        def flag = false


        println params
        println "AAA "+params.user.password.encodeAsSHA1()

//        def  resultTransformer

        def caggleUser = CaggleUser.createCriteria().list {
            and {
                eq("email", params.user.email)
                eq("password", params.user.password.encodeAsSHA1())
                eq("enabled", true)
            }
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            projections {
                    property('email', 'email')
                    property('username', 'username')

            }
        }

        println "New CCC >>>> " + caggleUser[0]

        if(caggleUser.size() >= 1){
            flag = true
        }
        def response = [:]
        if(flag) {
            response.code = 200
            response.currentUser = caggleUser[0]
        } else {
            response.code = 500
            response.message = "Error"
        }

        println response

        render response as JSON
    }
}
