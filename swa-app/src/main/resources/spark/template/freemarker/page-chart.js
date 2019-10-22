<#setting number_format="computer">
<script src="/js/chart-2.8.0.js"></script>
<script>
  <#list sections as section>
  <#if (section.chartElements)??>
  <#list section.chartElements as chart>
    new Chart(document.getElementById('${chart.id}').getContext('2d'), {
        type: 'line',
        data: {
            labels: [
            <#assign firstLabel=true>
            <#list chart.chartLabels as label>
              <#if firstLabel>
                <#assign firstLabel=false>
              <#else>
                ,
              </#if>
              '${label}'
            </#list>
            ],
            datasets: [
            <#assign firstDataset=true>
            <#list chart.datasets as dataset>
              <#if firstDataset>
                <#assign firstDataset=false>
              <#else>
                ,
              </#if>
              {
                label: '${dataset.label}',
                backgroundColor: '${dataset.color}',
                borderColor: '${dataset.color}',
                fill: false,
                lineTension: 0,
                data: [
                <#assign firstValue=true>
                <#list dataset.values as value>
                  <#if firstValue>
                    <#assign firstValue=false>
                  <#else>
                  ,
                  </#if>
                  ${value}
                </#list>
                ]
              }
            </#list>
            ]
        },
        options: {
          responsive: false,
          scales: {
            yAxes: [{
              ticks: {
                beginAtZero: true
              }
            }]
          },
          animation: {
            duration: 0
          },
          elements: {
            point:{
              radius: 0
            }
          }
        }
    });
  </#list>
  </#if>
  </#list>
</script>
