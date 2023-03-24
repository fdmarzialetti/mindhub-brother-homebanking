const { createApp } = Vue

createApp({
    data() {
        return {
            account: { "id": "", "number": "", "creationDate": "", "balance": "", "transactions": [] },
            client: "",
            clientName: "",
            startDate: "",
            endDate: ""
        }
    },
    created() {
        this.loadData();
    },
    mounted() {

    },
    methods: {
        loadData: function () {
            let urlParams = new URLSearchParams(document.location.search)
            let accountId = urlParams.get("accountId")
            axios
                .get("/api/clients/current")
                .then(res => {
                    this.client = res.data
                    this.getAccount(accountId)
                })
        },
        getAccount: function (id) {
            this.account = this.client.accounts.filter(a => a.id == id)[0]
            this.sortTransactions()
            this.renderChart()
        },
        formatCurrency: function (amount) {
            let options = { style: 'currency', currency: 'USD' };
            let numberFormat = new Intl.NumberFormat('en-US', options);
            return numberFormat.format(amount);
        },
        formatWord: function (word) {
            return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
        },
        renderChart: function () {
            let months = ["January", "February", "March", "April", "May", "June", 'July', 'August', 'September', 'October', 'November', 'December']
            let amountsByMonths = this.amountsByMonths(this.account.transactions)
            var options = {
                series: [{
                    name: "Balance",
                    data: amountsByMonths
                }],
                chart: {
                    type: 'bar',
                    height: 350
                },
                title: {
                    text: `Your ${new Date().getFullYear()} transactions balance`,
                    align: 'center',
                    margin: 10,
                    offsetX: 0,
                    offsetY: 0,
                    floating: false,
                    style: {
                        fontSize: '14px',
                        fontWeight: 'bold',
                        fontFamily: undefined,
                        color: '#fff'
                    },
                },
                plotOptions: {
                    bar: {
                        horizontal: true,
                        colors: {
                            ranges: [{
                                from: -Infinity,
                                to: 0,
                                color: '#FF4560'
                            }, {
                                from: 0.00001,
                                to: Infinity,
                                color: '#00E396'
                            }]
                        },
                    },
                },
                dataLabels: {
                    enabled: true,
                    formatter: function (val) {
                        return "" + val
                    },
                },
                yaxis: {
                    reversed: false,
                    labels: {
                        show: true,
                        style: {
                            colors: 'white',
                            fontSize: '12px',
                            fontFamily: 'Helvetica, Arial, sans-serif',
                            fontWeight: 400,
                            cssClass: 'apexcharts-xaxis-label',
                        },
                    }
                },
                xaxis: {
                    categories: months,
                    labels: {
                        style: {
                            colors: ["#FFFFFF"],
                            fontSize: '0px',
                            fontFamily: 'Helvetica, Arial, sans-serif',
                            fontWeight: 400,
                            cssClass: 'apexcharts-xaxis-label',
                        },
                    },
                },
                grid: {
                    xaxis: {
                        lines: {
                            show: true
                        },
                    }
                }

            };
            var chart = new ApexCharts(document.querySelector("#graph"), options);
            chart.render();
        },
        amountsByMonths: function (transactions) {
            let actualYear = new Date().getFullYear()
            let monthAccum = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
            transactions.forEach(t => {
                console.log(t.date.slice(0, 4))
                if (t.date.slice(0, 4) == actualYear) {
                    monthAccum[Number(t.date.slice(5, 7)) - 1] += t.amount
                } else {
                    return monthAccum
                }

            });
            return monthAccum
        },
        sortTransactions: function () {
            this.account.transactions.sort((t1, t2) => new Date(t2.date) - new Date(t1.date))
        },
        logout: function () {
            axios
                .post('/api/logout')
                .then(response => window.location.assign("/web/index.html"))
        },
        filterByDate: function () {
            console.log(typeof this.startDate)
            axios
                .get('/api/transactions', {
                    params: {
                        accountNumber: this.account.number,
                        startDate: this.startDate,
                        endDate: this.endDate
                    }
                }
                )
                .then(res => {
                    this.account.transactions = res.data.sort((t1, t2) => new Date(t2.date) - new Date(t1.date))
                })
        },
        printTable: function () {
            let element = document.getElementById('element-to-print');
            let opt = {
                margin: 0.12,
                filename: 'TransactionsSummary.pdf',
                image: { type: 'jpeg', quality: 0.98 },
                html2canvas: { scale: 1 },
                jsPDF: { unit: 'in', format: 'letter', orientation: 'portrait' }
            };
            html2pdf().set(opt).from(element).save();
        }
    }
}).mount('#app')
