App.onLoad(() => {
    const people = JSON.parse(Bridge.get("PeopleController", "getPeople"));

    if(people !== null ){
        people.forEach(person => {
            const html = `
                  <div class='people-card'>
                    <div class='info-card'>
                      <img src='data:image/png;base_64${person["picture"]}' alt='People Picture' class='profile-picture'/>
                      <div class='info'>
                        <p class='name'>${person["firstname"]} ${person["lastname"]}</p>
                        <p class='job'>${person["job"]}</p>
                      </div>
                      <div>
                        <button class='pen'>
                          <img src='assets/images/pen.svg' alt='Modify'/>
                        </button>
                      </div>
                    </div>
                    <div class='skill-people'>
                      <div class='skill-text-dev'>
                        <img src='assets/images/dev.svg' alt='dev-skill' class='skill-icon'/>
                        <p>Testing</p>
                      </div>
                      <div class='skill-text-support'>
                        <img src='assets/images/support.svg' alt='support-skill' class='skill-icon'/>
                        <p>Service Delivery</p>
                      </div>
                    </div>
                  </div>`;
            App.log(html);
            document.getElementById("container-personnel").innerHTML += html;
        });
    }



});