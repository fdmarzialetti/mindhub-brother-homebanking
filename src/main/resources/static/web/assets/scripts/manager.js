const { createApp } = Vue

createApp({
    data() {
        return {
            clients:"",
            response:"",
            email:"",
            newClient:{"firstName":"","lastName":"","email":""},
            newLoan:{"name":"","maxAmount":"","payments":[6],"percent":""},
            clientToEdit:{"id":"","firstName":"","lastName":"","email":""},
            loans:""

        }
    },
    created(){
        this.loadData();
    },
    methods:{
    
        validateInputs:function(clientInputs){
            let regex=/^[A-Za-z\s]*$/
            let regexEmail=/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
            if(clientInputs.firstName===""||clientInputs.lastName===""||clientInputs.email===""){
                alert("You must complete all fields")
                return false
            }
            if(!clientInputs.firstName.match(regex)){
                alert("the first name must contain only letters")
                return false
            }
            if(!clientInputs.lastName.match(regex)){
                alert("the last name must contain only letters")
                return false
            }
            if(!clientInputs.email.match(regexEmail)){
                alert("The email must have a valid format. example: email@gmail.com")
                return false
            }
            return true
        },
        clearForm:function(){
            this.newClient={"firstName":"","lastName":"","email":""}
        },
        loadData: function() {
                axios
                .get("http://localhost:8080/api/clients")
                .then(res=>{
                    this.clients=res.data
                    this.response=res
                    this.loadLoans()
                })
        },
        postClient:function(){
            axios
            .post("http://localhost:8080/rest/clients", this.newClient)
            .then(res=>{
                this.clearForm()
                this.loadData()})
            .then(alert(`you have added ${this.newClient.firstName} ${this.newClient.lastName} to the table`))
        },
        addClient:function(){
            if(this.validateInputs(this.newClient)){
                if(confirm(`Are you sure you want to add the next client?\nFirst name: ${this.newClient.firstName}\nLast name: ${this.newClient.lastName}\nEmail: ${this.newClient.email}\n`)){
                    this.postClient();
                }
            }     
        },
        deleteClient: function(client){
            console.log(client)
        if(confirm("Are you sure you want to delete "+client.firstName+" "+client.lastName+ " from the table?"))
            axios
            .delete("http://localhost:8080/rest/clients/"+client.id)
            .then(res=>this.loadData())
        },
        initializeClientToEdit:function(client){
            this.clientToEdit.firstName=client.firstName
            this.clientToEdit.lastName=client.lastName
            this.clientToEdit.email=client.email
            this.clientToEdit.id=client.id
        },
        editClient:function(){
            if(this.validateInputs(this.clientToEdit)){
                if(confirm("you want to save the changes?")){
                axios
                .patch("http://localhost:8080/rest/clients/"+this.clientToEdit.id,this.clientToEdit)
                .then(res=>{
                    this.loadData()
                    //devolver al modal la accion de cerrar.
                })
                } 
            }
        },
        loadLoans:function(){
            axios.get("http://localhost:8080/api/loans")
            .then(res=> {
                this.loans=res.data
            })
        },
        addLoan:function(){
                if(confirm('Are you sure you want to add a new Loan?')){
                    this.postLoan();
                }      
        },
        postLoan:function(){
            console.log(this.newLoan.payments)
            axios
            .post("http://localhost:8080/api/admin/loan", 
            "name="+this.newLoan.name+
            "&maxAmount="+this.newLoan.maxAmount+
            "&payments="+this.newLoan.payments+
            "&percent="+this.newLoan.percent
            )
            .then(res=>{
                this.clearForm()
                this.loadData()
                console.log(res)
            })
            .then(alert(`you have added a new loan`))
        },
        setPayments:function(event){
            this.newLoan.payments=JSON.parse(event.target.value)
            console.log(this.newLoan.payments)
        }

    }
}).mount('#app')