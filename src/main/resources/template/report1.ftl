{
     title: {
        left: 'center',
        text: '${title}',
    },
    tooltip : {
        trigger: 'axis',
        axisPointer: {
            type: 'cross',
            label: {
                backgroundColor: '#6a7985'
            }
        }
    },
     grid: {
        show:true
    },
    xAxis: {
        type: 'category',
        data: ${categories}
    },
    yAxis: {
        name:' (百分比) ',
        type: 'value'
    },
    
    series: [{
        type:'line',
        data: ${values},
        type: 'line',
        label: {
                normal: {
                    show: true,
                    position: 'top'
                }
            },
        color:'blue'    
    }]
}