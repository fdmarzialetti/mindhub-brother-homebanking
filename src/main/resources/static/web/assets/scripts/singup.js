const { createApp } = Vue

createApp({
    data() {
        return {
            firstName: "",
            lastName: "",
            email: "",
            password: ""
        }
    },
    created() {

    },
    mounted() {

    },
    methods: {
        singup: function () {
            axios
                .post('/api/clients', "firstName=" + this.firstName + "&lastName=" + this.lastName + "&email=" + this.email + "&password=" + this.password,
                    { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    console.log(response)
                    this.login()
                })
                .catch(function (error) {
                    document.getElementById("response-msg").innerHTML = error.response.data;
                });
        },
        login: function () {
            axios
                .post('/api/login', "email=" + this.email + "&password=" + this.password,
                    { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    window.location.assign(response.request.responseURL)
                })
                .catch(function (error) {
                    console.log(error);
                });
        }
    }
}).mount('#app')
