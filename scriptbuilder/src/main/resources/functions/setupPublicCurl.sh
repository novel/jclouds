alias apt-get-install="apt-get install -f -y -qq --force-yes"
alias apt-get-upgrade="(apt-get update -qq&&apt-get upgrade -y -qq)"

function ensure_cmd_or_install_package_apt(){
  local cmd=$1
  local pkg=$2
  
  hash $cmd 2>/dev/null || apt-get-install $pkg || ( apt-get-upgrade && apt-get-install $pkg )
}

function ensure_cmd_or_install_package_yum(){
  local cmd=$1
  local pkg=$2
  hash $cmd 2>/dev/null || yum --nogpgcheck -y ensure $pkg
}

function ensure_netutils_apt() {
  ensure_cmd_or_install_package_apt nslookup dnsutils
  ensure_cmd_or_install_package_apt curl curl
}

function ensure_netutils_yum() {
  ensure_cmd_or_install_package_yum nslookup bind-utils
  ensure_cmd_or_install_package_yum curl curl
}

# most network services require that the hostname is in
# the /etc/hosts file, or they won't operate
function ensure_hostname_in_hosts() {
  egrep -q `hostname` /etc/hosts || awk -v hostname=`hostname` 'END { print $1" "hostname }' /proc/net/arp >> /etc/hosts
}

# download locations for many services are at public dns
function ensure_can_resolve_public_dns() {
  nslookup yahoo.com > /dev/null || echo nameserver 208.67.222.222 >> /etc/resolv.conf
}

function setupPublicCurl() {
  ensure_hostname_in_hosts
  if hash apt-get 2>/dev/null; then
    ensure_netutils_apt
  elif hash yum 2>/dev/null; then
    ensure_netutils_yum
  else
    abort "we only support apt-get and yum right now... please contribute!"
    return 1
  fi
  ensure_can_resolve_public_dns
  return 0  
}
