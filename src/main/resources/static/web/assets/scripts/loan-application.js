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
                .get("http://localhost:8080/api/loans")
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
            .get("http://localhost:8080/api/clients/current/accounts")
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
                    )
                    window.location.assign("/web/accounts.html")
                } catch (error) {
                    console.log(error)
                }
            }
            if (this.amount == "") {
                alert("you must complete all the fields")
            } else {
                if (confirm("you want to apply loan?")) {
                    applyLoan()
                }
            }
        },
    }
}).mount('#app')