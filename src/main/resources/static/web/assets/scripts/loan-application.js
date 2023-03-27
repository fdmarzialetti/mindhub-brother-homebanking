const { createApp } = Vue

createApp({
    data() {
        return {
            clientAccounts:"",
            account:"",
            loans:"",
            selectedLoan:"",
            payments:"",
            type:"",
            payment:"",
            amount:"",
            maxAmount:"",
            percent:""
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData:function(){
            axios
                .get("/api/loans")
                .then(res => {
                    this.loans=res.data
                })
                .then(res=>{
                    this.selectedLoan=this.loans[0]
                    this.type = this.selectedLoan.name
                    this.payments = this.loans[0].payments
                    this.payment = this.payments[0]
                    this.maxAmount = this.selectedLoan.maxAmount
                })
            axios
            .get("/api/clients/current/accounts")
            .then(res=>{
                this.clientAccounts=res.data
                this.account=this.clientAccounts[0]
            })
        },
        selectLoan:function(){
            this.selectedLoan = this.loans.filter(l=>l.name==this.type)[0]
            this.maxAmount = this.selectedLoan.maxAmount
            this.payments = this.selectedLoan.payments
            this.payment = this.payments[0]
            this.amount=""
        },
        formatCurrency: function (amount) {
            let options = { style: 'currency', currency: 'USD' };
            let numberFormat = new Intl.NumberFormat('en-US', options);
            return numberFormat.format(amount);
        },
        applyLoan: function () {
            const applyLoan = async () => {
                try {
                    const res = await axios.post('/api/loans',
                    {
                        "id":this.selectedLoan.id,
                        "amount":this.amount,
                        "payments":this.payment,
                        "accountNumber":this.account.number
                    }
                    ).then(()=>{
                        Swal.fire({
                            title:'Successfull apply loan',
                            icon: 'success',
                        })
                        .then(()=>window.location.assign("/web/accounts.html"))
                    }) 
                } catch (error) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Apply loan error',
                            text: error.response.data,
                        })
                    console.log(error)
                }
            }
            if (this.amount == "") {
                Swal.fire({
                    icon: 'error',
                    title: 'Apply loan error!',
                    text: 'You must complete all the fields',
                })
            } else {
                Swal.fire({
                    title: 'Are you sure to request a loan?',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Send'
                }).then((result) => {
                    if (result.isConfirmed){
                        applyLoan()
                    }
                })
            }
        },
    }
}).mount('#app')