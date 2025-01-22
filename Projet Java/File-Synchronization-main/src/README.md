## Utilisation
Ce manuel fournit des instructions pour lancer l'application Java à partir de la ligne de commande. Les exemples donnés illustrent comment lancer l'application avec ou sans interface graphique (GUI) et comment ignorer certains fichiers lors de la copie du contenu du dossier source vers le dossier cible. Suivez les instructions ci-dessous pour lancer l'application avec les options souhaitées.
Tout le contenu du dossier _SOURCE_ sera copié vers le dossier _TARGET_ et les éléments n'y figurant pas seront supprimés.

```shell
Usage: java -jar build.jar [gui|nogui] [source] [target] ([ignored files])
```
Double cliquer sur le jar fonctionne aussi. (Revient à: )
```shell
java -jar build.jar
```

## Exemples :
1. Lancement avec l'interface graphique (GUI) en précisant le dossier 'source' sur le bureau et le dossier 'target' sur le bureau, en ignorant tous les fichiers qui s'appellent '.DS_Store', tous les fichiers html et le fichier 'style.css'
```shell
java -jar build-latest.jar gui /Users/loicderghoum/Desktop/source /Users/loicderghoum/Desktop/target "[.DS_Store, *.html, style.css]"
```

2. Lancement sans l'interface graphique (non-GUI) en précisant le dossier 'source' sur le bureau et le dossier 'target' sur le bureau, en ignorant tous les fichiers qui s'appellent ".DS_Store", tous les fichiers html et le fichier "style.css"
```shell
java -jar build-latest.jar nogui /Users/loicderghoum/Desktop/source /Users/loicderghoum/Desktop/target "[.DS_Store, *.html, style.css]"
```

3. Lancement avec l'interface graphique (GUI) en précisant le dossier 'source' sur le bureau et le dossier 'target' sur le bureau, en ignorant tous les fichiers html
```shell
java -jar build-latest.jar gui /Users/loicderghoum/Desktop/source /Users/loicderghoum/Desktop/target "[*.html]"
```

## Extra features

1. Un fichier journal (créé lors du premier lancement de l'application) permet de suivre en temps réel toutes les actions réalisées, telles que la copie de fichiers, la suppression de fichiers et les erreurs rencontrées.

2. Le fichier config.properties (créé lors du premier lancement de l'application) permet de sauvegarder les dernières valeurs enregistrées par l'utilisateur. Ces valeurs sont automatiquement chargées lors du prochain démarrage de l'application.

3. Il est possible d'ignorer des fichiers ou des dossiers en spécifiant un nom précis (fichier.ext) ou une extension (*.ext).

4. Une interface graphique facultative est disponible pour lancer l'application. Elle permet une utilisation plus simple de l'application.

## IMPORTANT
La liste des fichiers à ignorer est facultative, mais si elle est spécifiée, elle doit être placée entre guillemets doubles " (pour indiquer qu'il s'agit d'un seul argument pour l'ensemble de la liste), entre crochets (pour indiquer qu'il s'agit d'une liste), **ET CHAQUE FICHIER DOIT ÊTRE SÉPARÉ PAR UNE VIRGULE ET UN ESPACE (', ')**
Ex : "[main.c, *.pdf]"
_Cela ignorerait tous les fichiers nommés "main.c" et tous les fichiers pdf._
