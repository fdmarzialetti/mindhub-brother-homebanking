const { createApp } = Vue;

createApp({
    data() {
        return {
            logEmail: "",
            logPass: "",
            firstName: "",
            lastName: "",
            email: "",
            password: "",
            log: true,
        }
    },
    created() {

    },
    mounted() {

    },
    methods: {
        login: function () {
            axios
                .post('/api/login', "email=" + this.logEmail + "&password=" + this.logPass,
                    { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    Swal.fire({
                        title: 'Login Success',
                        icon: 'success',
                    }).then( ()=>{
                        console.log(response)
                        window.location.assign(response.headers.res)})
                })
                .catch(error => {
                    Swal.fire({
                        title: 'Login denied',
                        text: 'Please verify the input data',
                        icon: 'error',
                    })
                }
                )
        },
        singup: function () {
            axios
                .post('/api/clients', "firstName=" + this.firstName + "&lastName=" + this.lastName + "&email=" + this.email + "&password=" + this.password,
                    { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(() => {
                    this.loginAfterSingup()
                })
                .catch(function (error) {
                    document.getElementById("response-msg").innerHTML = error.response.data;
                });
        },
        // validateInputsLogin: function () {
        //     let regexEmail = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        //     if (this.logPass === "" || this.logEmail === "") {
        //         alert("You must complete all fields")
        //         return false
        //     }
        //     if (!this.logEmail.match(regexEmail)) {
        //         alert("The email must have a valid format. example: email@gmail.com")
        //         return false
        //     }
        //     return true
        // },
        // validateInputsSingup: function () {
        //     let regexEmail = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        //     if (this.firstName === "" || this.lastName === ""|| this.email==="" || this.password==="") {
        //         alert("You must complete all fields")
        //         return false
        //     }
        //     if (!this.email.match(regexEmail)) {
        //         alert("The email must have a valid format. example: email@gmail.com")
        //         return false
        //     }
        //     return true
        // },
        loginAfterSingup: function () {
            axios
                .post('/api/login', "email=" + this.email + "&password=" + this.password,
                    { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    window.location.assign(response.headers.res)
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        changeLogin: function () {
            this.log = !this.log
        }
    }
}).mount('#app')
