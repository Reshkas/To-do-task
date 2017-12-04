loadNotes();

const newNoteSave = document.querySelector('.note-create-tool-save');
newNoteSave.addEventListener('click', function (event) {
  const newTitle = document.querySelector('.note-create-title');
  const newDescription = document.querySelector('.note-create-description');
  const newColor = document.querySelector('.note-create-tool-color');

  if (!newTitle.value || !newDescription.value || !newColor.value) {
    return false;
  }

  const note = {
    title: newTitle.value,
    description: newDescription.value,
    color: newColor.value
  };
  
  addNote(note).then(() => {
    loadNotes();
  });

  newTitle.value = '';
  newDescription.value = '';
  newColor.value = '#abce6b';
});

function loadNotes() {
  fetch('/notes', {method: 'GET'})
    .then(function(res){return res.json()} )
    .then(function(data) {
      const noteList = document.querySelector('.note-list');
      while (noteList.hasChildNodes()) {
        noteList.removeChild(noteList.lastChild);
      }
      data.map(function (note) {
        noteList.appendChild(createArticle(note))
      });
    })
    .catch(function(err){
      console.log(err);
    });
};

function addNote(note) {
  const data = 'note=' + encodeURIComponent(JSON.stringify(note));
  const requestParams = {
    method: 'POST',
    headers: {'Content-Type':'application/x-www-form-urlencoded'},
    body: data
  };
  return fetch('/notes', requestParams);
};

function createArticle(note) {
  const article = document.createElement('article');
  article.className = 'note-list-item';
  article.style.backgroundColor = note.color;

  const title = document.createElement('h3');
  title.className = 'note-list-item-title';
  title.innerHTML = note.title;

  article.appendChild(title);

  const description = document.createElement('p');
  description.className = 'note-list-item-description';
  description.innerHTML = note.description;

  article.appendChild(description);

  const button = document.createElement('button');
  button.className = 'note-list-item-edit button';
  button.innerHTML = 'Edit';
  button.style.backgroundColor = note.color;
  button.addEventListener('mouseenter', function (event){
    event.target.style.backgroundColor = 'rgba(0, 0, 0, 0.2)';
  });
  button.addEventListener('mouseleave', function(event){
    event.target.style.backgroundColor = note.color;
  });

  article.appendChild(button);

  addIsActiveEvent(article);

  return article;
}

function addIsActiveEvent(element) {
  element.addEventListener('mouseenter', function(event) {
    const elements = event.target.children;
    Array.from(elements).forEach(function(element){
      if (element.className.includes('note-list-item-edit')) {
        element.classList.add('is-active');
      }
    });
  });

  element.addEventListener('mouseleave', event => {
    const elements = event.target.children;
    Array.from(elements).forEach(element => {
      if (element.className.includes('note-list-item-edit')) {
        element.classList.remove('is-active');
      }
    });
  });
};
