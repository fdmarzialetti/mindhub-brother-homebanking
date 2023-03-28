const { createApp } = Vue

createApp({
    data() {
        return {
            client: {},
            debitCards: [],
            creditCards: [],
            expiredCredit: [],
            expiredDebit: [],
            load: false
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
                .get("/api/clients/current")
                .then(res => {
                    this.client = res.data
                    this.creditCards = this.client.cards.filter(c => c.type === "CREDIT")
                    this.debitCards = this.client.cards.filter(c => c.type === "DEBIT")
                    this.expiredCreditCards();
                    this.expiredDebitCards();
                }).then(() => this.load = true)
        },
        filterCards: function () {
            this.creditCards = this.client.cards.filter(c => c.type === "CREDIT")
            this.debitCards = this.client.cards.filter(c => c.type === "DEBIT")
        },
        formatCurrency: function (amount) {
            let options = { style: 'currency', currency: 'USD' };
            let numberFormat = new Intl.NumberFormat('en-US', options);
            return numberFormat.format(amount);
        },
        formatWord: function (word) {
            return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
        },
        logout: function () {
            axios
                .post('/api/logout')
                .then(response => window.location.assign("/web/index.html"))
        },
        deleteCard: function (card) {
            let cardID = card.id;
            const deleteCard = async () => {
                try {
                    const res = await axios.post("/api/clients/current/deleteCard","cardID="+cardID)
                        .then(res => {
                            console.log(res)
                            Swal.fire({
                                title: 'Deleted!',
                                text: 'The card ' + card.number + ' has been deleted.',
                                icon: 'success'
                            })
                            .then(() => window.location.assign("/web/cards.html"))
                        }).catch(erro=>{
                            console.log("catch error")
                            console.log(erro)})
                        } catch (error) {
                            Swal.fire({
                                icon: 'error',
                                title: 'Delete card error',
                                text: error.response,
                            })
                            console.log(error)
                        }
                }
                Swal.fire({
                    title: 'Are you sure to delete this card?',
                    text: "Once deleted, you will not be able to recover this card!",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Delete'
                }).then((result) => {
                    if (result.isConfirmed) {
                        deleteCard()
                    }
                })
            },
                expiredCreditCards: function () {
                    let actualDate = new Date()
            actualDate = actualDate.getFullYear() + "-" + (1 + actualDate.getMonth()) + "-" + actualDate.getDate()
            this.expiredCredit = this.creditCards.map(c => c.thruDate < actualDate)
    },
    expiredDebitCards: function () {
        let actualDate = new Date()
        actualDate = actualDate.getFullYear() + "-" + (1 + actualDate.getMonth()) + "-" + actualDate.getDate()
        this.expiredDebit = this.debitCards.map(c => c.thruDate < actualDate)
    },
}
}).mount('#app')
