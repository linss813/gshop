export default {
  data() {
    return {
      quickActions: [/* ... */],
      staticStats: [
        { label: '总用户数', value: 0, styleClass: 'primary' },
        { label: '总商品数', value: 0, styleClass: 'secondary' },
        { label: '总销售额', value: 0, styleClass: 'success' },
        { label: '预警商品', value: 0, styleClass: 'danger' }
      ],
      warningGoods: []
    };
  },
  created() {
    this.fetchStaticStats();
    this.fetchWarningGoods();
  },
  methods: {
    async fetchStaticStats() {
      const [userRes, productRes, salesRes, alertRes] = await Promise.all([
        axios.get('/user/countUsers'),
        axios.get('/product/count'),
        axios.get('/order/getTotalSales'),
        axios.get('/product/alertCount')
      ]);
      
      this.staticStats[0].value = userRes.data.data;
      this.staticStats[1].value = productRes.data.data;
      this.staticStats[2].value = salesRes.data.data;
      this.staticStats[3].value = alertRes.data.data;
    },
    async fetchWarningGoods() {
      const res = await axios.get('/product/alertList');
      this.warningGoods = res.data.data;
    }
  }
};