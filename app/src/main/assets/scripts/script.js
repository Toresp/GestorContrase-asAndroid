const date1 = new Date();
var obj;
const renderCalendar = () => {
  date1.setDate(1);
  const diasMes = document.querySelector(".days");
  const ultimoDia = new Date(date1.getFullYear(), 
  date1.getMonth()+1,0).getDate();
  const prevUltimoDia = new Date(date1.getFullYear(), 
  date1.getMonth(),0).getDate();
  
  const PrimerDia = date1.getDay();
  const ultimoDiaIndex =  new Date(
    date1.getFullYear(),
    date1.getMonth() + 1,
    0
  ).getDay();
  
  const nextDias = 7 - ultimoDiaIndex - 1;
  
  const meses = [
  "Enero",
  "Febrero",
  "Marzo",
  "Abril",
  "Mayo",
  "Junio",
  "Julio",
  "Agosto",
  "Septiembre",
  "Octubre",
  "Noviembre",
  "Diciembre"
  ];
    
  document.querySelector('.date h1').innerHTML 
  = meses[date1.getMonth()] +" "+date1.getFullYear().toString();

  new Date().toDateString();
  document.querySelector('.date p').innerHTML 
  = cambiarIdioma();

  let dias = "";
//Introduce los ultimos dias de el mes anterior de el mes anterior.
  for(let x = PrimerDia; x > 0; x--){
    
    dias += `<div class="prev-date">${prevUltimoDia - x + 1}</div>`;
  };
//Carga los dias en el calendario marca en verde el dia de hoy y los correspondientes dias almacenados en la nube.
  
  for(let i = 1; i <= ultimoDia;i++){
      if(i === new Date().getDate() && date1.getMonth() === new Date().getMonth() && date1.getFullYear() === new Date().getFullYear()){
        var found = new Boolean(false)
        for(let l = 0; l < obj.length;l++){
          const fecha = new Date(obj[l].date);
          if((date1.getMonth() === fecha.getMonth()) && (i === fecha.getDate())){
            dias += `<div class="todayEvent" id ="dia" data-title="${obj[l].event}">${i}</div>`;
            alert("Debes cambiar la contraseña!!");
            found=true;
          }
        }
        if(found==false){
            dias += `<div class="today" id ="dia">${i}</div>`;
        }
      }else{
          var found = new Boolean(false)
          for(let k = 0; k < obj.length;k++){
            const fecha = new Date(obj[k].date);
            if((date1.getMonth() === fecha.getMonth()) && (i === fecha.getDate()) && (date1.getFullYear() === fecha.getFullYear())){
              dias += `<div class="event" id ="dia" data-title="${obj[k].event}">${i}</div>`;
              found=true;
            }
          }
          if(found == false){
            dias += `<div id ="dia">${i}</div>`;
          }
      }

  };

//Introduce los primeros dias de el mes siguiente
  for(let j = 1; j <= nextDias; j++){
    dias += `<div class="next-date">${j}</div>`;
    diasMes.innerHTML = dias;
  };


  function cambiarIdioma(){
    d = new Date();
    switch(d.getDay()){
      case 0: return traduccion ="Domingo, "+d.getDate() +" de "+ meses[d.getMonth()] + " del "+ d.getFullYear();
      case 1: return traduccion ="Lunes, "+d.getDate() +" de "+ meses[d.getMonth()] + " del "+ d.getFullYear();
      case 2: return traduccion ="Martes, "+d.getDate() +" de "+ meses[d.getMonth()] + " del "+ d.getFullYear();
      case 3: return traduccion ="Miercoles, "+d.getDate() +" de "+ meses[d.getMonth()] + " del "+ d.getFullYear();
      case 4: return traduccion ="Jueves, "+d.getDate() +" de "+ meses[d.getMonth()] + " del "+ d.getFullYear();
      case 5: return traduccion ="Vienres, "+d.getDate() +" de "+ meses[d.getMonth()] + " del "+ d.getFullYear();
      case 6: return traduccion ="Sabado, "+d.getDate() +" de "+ meses[d.getMonth()] + " del "+ d.getFullYear();
    }
  }
}



document.querySelector('.prev').addEventListener('click',()=>{
  date1.setMonth(date1.getMonth() - 1);
  renderCalendar();
});

document.querySelector('.next').addEventListener('click',()=>{
  date1.setMonth(date1.getMonth() + 1);
    renderCalendar();
});

obj = Android.getJson();
renderCalendar(data);


              





