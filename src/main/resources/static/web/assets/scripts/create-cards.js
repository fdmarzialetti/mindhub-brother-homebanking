const { createApp } = Vue

createApp({
    data() {
        return {
            color:"",
            type:"",
            client:"",
            validCard:true
        }
    },
    created() {
        this.loadData()
    },
    mounted() {

    },
    methods: {   
        loadData:function(){
            axios
            .get("http://localhost:8080/api/clients/current")
                .then(res => {
                    this.client = res.data
                })
        },
        createCard:function(){
            let cardColor = this.color.toLowerCase()
                    cardColor = cardColor.charAt(0).toUpperCase() + cardColor.slice(1)
                    let cardType = this.type.toLowerCase()
                    cardType = cardType.charAt(0).toUpperCase() + cardType.slice(1)
            const postCard = async () => {
                try {
                    const res = await axios.post('http://localhost:8080/api/clients/current/cards', "cardType="+this.type+"&colorType="+this.color)
                    Swal.fire({
                                title:'Created!',
                                text:cardColor+' '+cardType+' card has been created.',
                                icon:'success'
                        }).then(res=>window.location.assign("/web/cards.html"))
                } catch (error) {
                    console.log(error)
                    document.getElementById("errorMsg").innerHTML=error.response.data
                }
            }
            Swal.fire({
                title: 'Are you sure create a '+cardColor+' '+cardType+' card?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Create card'
            }).then((result) => {
                if (result.isConfirmed) {  
                    postCard()        
                }
            })      
        },
        setColor:function(event){
            this.color=event.target.id
            if(this.type==""){

            }else{
                this.checkAvaliableCard()
            }
            
        },
        setType:function(event){
            this.type=event.target.id
            if(this.color==""){

            }else{
                this.checkAvaliableCard()
            }
        },
        logout: function () {
            axios
                .post('/api/logout')
                .then(response => window.location.assign("/web/index.html"))
        },
        checkAvaliableCard:function(){
            if(this.client.cards.some(c=>c.color==this.color&&c.type==this.type)){
                let warningMsg = "Already have a this card ..."
                this.validCard=false
                document.getElementById("warningMsg").innerHTML=warningMsg
            }else{
                let warningMsg = "You can request this card!"
                this.validCard=true
                document.getElementById("warningMsg").innerHTML=warningMsg
            }
        }
    }
}).mount('#app')