# Use a imagem oficial do Oracle Database Express Edition como base
# Pode usar 'latest' ou uma versão específica como '21.3.0-xe'
FROM container-registry.oracle.com/database/express:21.3.0-xe

# Mantenedor (opcional)
LABEL maintainer="Seu Nome dephenriquejp@gmail.com"

# Define um diretório de trabalho. O Oracle usa /opt/oracle internamente, mas podemos definir um customizado.
WORKDIR /opt/oracle/mydata_dir

# A imagem base já configura o usuário 'oracle' que é um usuário não-root no container.
# O processo do banco de dados rodará como 'oracle'.
# Não é necessário criar um novo usuário aqui para cumprir o requisito de 'usuário não root'.

# Expõe a porta padrão do Listener Oracle
EXPOSE 1521

# Se você tiver scripts SQL para inicializar o banco de dados (ex: criar um tablespace, usuários customizados, etc.),
# você pode copiá-los para o diretório de scripts de inicialização da imagem base:
# COPY init-user-and-tables.sql /opt/oracle/scripts/setup/

# O comando padrão (CMD) da imagem base já inicia o serviço Oracle.