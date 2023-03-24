const { createApp } = Vue

createApp({
    data() {
        return {
            client: { "id": "", "firstName": "", "lastName": "", "accounts": [], "loans": [] },
            totalBalance: 0,
            totalCredits: 0,
            totalDebits: 0,
            selectedAccount: { "id": "", "number": "", "creationDate": "", "balance": "", "transactions": [], },
            percentDebits: 0,
            percentCredits: 0,
            incomeRangeDate: "year",
            spendingRangeDate: "year",
        }
    },
    created() {
        this.loadData();
    },
    mounted() {
    },
    methods: {
        loadData: function () {
            axios
                .get("http://localhost:8080/api/clients/current")
                .then(res => this.client = res.data)
                .then(res => {
                    if (this.client.accounts.length !== 0) {
                        this.selectedAccount = this.client.accounts[0]
                    }
                    this.calculateTotalBalance()
                    this.calculateStats()
                })
        },
        calculateTotalBalance: function () {
            this.totalBalance = Array.from(this.client.accounts).reduce((a, b) => a + b.balance, 0)
        },
        calculateStats: function () {
            let totalCredits = 0
            let totalDebits = 0
            let accounts = Array.from(this.client.accounts)
            accounts.forEach(a => a.transactions.forEach(t => {
                if (t.type === "CREDIT") {
                    totalCredits += t.amount
                } else {
                    totalDebits += t.amount * -1
                }
            }))
            this.totalCredits = totalCredits;
            this.totalDebits = totalDebits
            this.percentCredits = ((totalCredits * 100) / this.totalBalance).toFixed(2)
            this.percentDebits = ((totalDebits * 100) / this.totalBalance).toFixed(2)
        },
        joinAllTransactions: function () {

        },
        formatCurrency: function (amount) {
            let options = { style: 'currency', currency: 'USD' };
            let numberFormat = new Intl.NumberFormat('en-US', options);
            return numberFormat.format(amount);
        },
        formatWord: function (word) {
            return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
        },
        selectAccount: function (event) {
            this.selectedAccount = this.client.accounts.find(a => a.number === event.target.value)
        },
        calculateSelectedStats: function (typeTransaction, event) {
            let dateValue = event.target.value
            let now = new Date()
            let day = now.getDate()
            let month = now.getMonth() + 1
            let year = now.getFullYear()
            let total = 0
            let accounts = Array.from(this.client.accounts)
            if (dateValue === "year") {
                accounts.forEach(a => a.transactions.forEach(t => {
                    if (t.type === typeTransaction && Number(t.date.slice(0, 4)) == year) {
                        total += t.amount
                    }
                }
                ))
            } else {
                if (dateValue === "month") {
                    accounts.forEach(a => a.transactions.forEach(t => {
                        if (t.type === typeTransaction && Number(t.date.slice(0, 4)) == year && Number(t.date.slice(5, 7)) == month) {
                            total += t.amount
                        }
                    }
                    ))
                } else {
                    accounts.forEach(a => a.transactions.forEach(t => {
                        if (t.type === typeTransaction && Number(t.date.slice(0, 4)) == year && Number(t.date.slice(5, 7)) == month && Number(t.date.slice(8, 10)) == day) {
                            total += t.amount
                        }
                    }
                    ))
                }
            }
            if (typeTransaction === "CREDIT") {
                this.totalCredits = total
                this.percentCredits = ((total * 100) / this.totalBalance).toFixed(2)
            } else {
                this.totalDebits = total * -1
                this.percentDebits = ((total * 100 * -1) / this.totalBalance).toFixed(2)
                if (total === 0) {
                    this.totalDebits = total
                }
            }
        },
        logout: function () {
            axios
                .post('/api/logout')
                .then(response => window.location.assign("/web/index.html"))
        },
        createNewAccount: function (type) {
            let accountType = type.toLowerCase()
            accountType = accountType.charAt(0).toUpperCase() + accountType.slice(1)
            const postAccount = async () => {
                try {
                    const res = await axios.post("/api/clients/current/accounts", "accountType=" + type)
                    Swal.fire({
                        title: accountType+' account created!',
                        icon: 'success',
                    })
                    .then(res=>location.reload())
                } catch (error) {
                    console.log(error)
                }
            }
            Swal.fire({
                title: 'Are you sure to create a new '+type.toLowerCase()+' account?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Create account'
            }).then((result) => {
                if (result.isConfirmed){
                    postAccount()
                }
            })
        },
        deleteAccount: function (acc) {
            Swal.fire({
                title: 'Are you sure to delete \nthe ' + acc.number + ' account?',
                text: "Once deleted, you will not be able to recover this account!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Delete'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios
                        .post("/api/clients/current/deleteAccount", "accountNumber=" + acc.number + "&accountID=" + acc.id)
                        .then(res => {
                            Swal.fire({
                                title:'Deleted!',
                                text:'The account ' + acc.number + ' has been deleted.',
                                icon:'success'
                        })
                                .then(res => window.location.assign("/web/accounts.html"))
                        })
                        .catch(error => {
                            swal.fire(error.data, {
                                icon: "error",
                            })
                        })
                }
            })

        },
    }
}).mount('#app')

