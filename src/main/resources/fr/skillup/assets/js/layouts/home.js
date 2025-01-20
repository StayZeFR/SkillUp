
//Affichage des skills sur les people cards
document.querySelectorAll('.people-card').forEach(card => {
    const skill-dev = card.querySelectorAll('.skill-text-dev');
    const skill-support = card.querySelectorAll('.skill-text-support');
    const skill-management = card.querySelectorAll('.skill-text-management');
    const skill-governance = card.querySelectorAll('.skill-text-governance');
    const skill-strategy = card.querySelectorAll('.skill-text-strategy');
    const moreSkills = card.querySelector('.more-skills');

    if (skill-dev.length || skill-support || skill-management || skill-governance || skill-strategy > 2) {
        skills.forEach((skill, index) => {
            if (index >= 2) {
                skill.classList.add('hidden');
            }
        });
        moreSkills.textContent = '+${skills.length - 2} autre';
    } else {
        moreSkills.style.display = 'none';
    }
});