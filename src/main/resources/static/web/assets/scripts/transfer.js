const { createApp } = Vue

createApp({
    data() {
        return {
            client: "",
            originNumber: "",
            destinationNumber:"",
            amount: "",
            description: "",
            radio: "another",
            leftAccounts: "",
        }
    },
    created() {
        this.loadData()
    },
    mounted() {

    },
    beforeMounted(){

    },
    methods: {
        loadData: function () {
            axios
                .get("/api/clients/current")
                .then(res => {
                    this.client = res.data     
                })
                .then(res=>{
                    let url = window.location.href;
                    if (url.includes('?')) {
                        this.originNumber =  new URLSearchParams(document.location.search).get("account")
                        this.setLeftAccounts()
                    } else{
                        this.originNumber = this.client.accounts[0].number;
                        this.setLeftAccounts()
                    } 
                })
        },
        transfer: function () {
            const transfer = async () => {
                try {
                    const res = await axios.post('/api/transactions',
                        "originNumber=" + this.originNumber +
                        "&destinationNumber=" + this.destinationNumber +
                        "&amount=" + this.amount +
                        "&description=" + this.description
                    )
                    window.location.assign("/web/accounts.html")
                } catch (error) {
                    console.log(error)
                }
            }
            if (this.originNumber == "" || this.destinationNumber == "" || this.amount == "" || this.description == "") {
                alert("you must complete all the fields")
            } else {
                if (confirm("you want to transfer?")) {
                    transfer()
                }
            }
        },
        setLeftAccounts: function () {
            this.leftAccounts = this.client.accounts.filter(a => a.number != this.originNumber)
        },
        setRadio: function (event) {
            if(event.target.id=="own"){
                this.setLeftAccounts()
                this.destinationNumber=this.leftAccounts[0].number
            }else{
                this.destinationNumber="";
            }
            
            this.radio = event.target.id
        },
    }
}).mount('#app')