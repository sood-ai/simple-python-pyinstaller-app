#!groovy
/* groovylint-disable CompileStatic, DuplicateStringLiteral, NestedBlockDepth */
//server = Artifactory.server 'ocrepo'

pipeline {
    agent { label 'jenkins-fat' }

    environment {
        OCREPO_DEPLOY = credentials('jnknsb_art_p_lhind_projex_sync')
        OC_CLUSTER   = 'insecure://master.ocp02.cloud.lhind.app.lufthansa.com'
        OC_CREDENTIALS = 'projex-int-sa'
        OC_TOKEN = credentials('projex-int-token')
        OC_NAMESPACE    = 'projex-int'
        OC_STAGE    = 'integration'

        REPO_GROUP   = 'CUS-LHIND-PROJEX'
        REPO_NAME    = 'lhind-projex-main'
        APP_NAME     = 'projex-backend'
        OCGIT        = 'git01.lhind.app.lufthansa.com'
        PORT         = '8080'
        VERSION      = '1.1.5'
    }

    /* ***** ***** ***** ***** ***** ***** STAGES ***** ***** ***** ***** ***** ***** */

    stages {
        //  stage('Cleanup. Delete this stage after all worked') {
        //      steps {
        //          script {
        //              echo "deleting objects in: ${OC_NAMESPACE}"
        //              openshift.withCluster(OC_CLUSTER) {
        //                  openshift.withCredentials(OC_CREDENTIALS) {
        //                      openshift.withProject(OC_NAMESPACE) {
        //                          openshift.selector("all", [ app : "${APP_NAME}" ]).delete();
        //                      }
        //                  }
        //              }
        //          }
        //      }
        //  }

        // --- Only needed at first build in namespace ---
        stage('Register helm repository') {
            steps {
                script {
                    openshift.withCluster(OC_CLUSTER) {
                        openshift.withCredentials(OC_CREDENTIALS) {
                            openshift.withProject(OC_NAMESPACE) {
                                sh 'helm repo add devopsplatform-lhind-px-helm-all https://repo01.lhind.app.lufthansa.com/artifactory/devopsplatform-lhind-px-helm-all --username ${OCREPO_DEPLOY_USR} --password ${OCREPO_DEPLOY_PSW}'
                            }
                        }
                    }
                }
            }
        }

        stage('Config OpenShift Resources via Helm ⎈ ') {
            steps {
                script {
                    openshift.withCluster(OC_CLUSTER) {
                        openshift.withCredentials(OC_CREDENTIALS) {
                            openshift.withProject(OC_NAMESPACE) {
                                //sh 'helm dependency update deployment/helm-backend/'
                                sh """
                                helm upgrade --install ${APP_NAME} --namespace=${OC_NAMESPACE} \
                                deployment/helm-backend/ -f deployment/helm-backend/values.yaml -f deployment/helm-backend/values.${OC_STAGE}.yaml \
                                --kube-apiserver https://master.ocp02.cloud.lhind.app.lufthansa.com \
                                --kube-token ${OC_TOKEN} \
                                --disable-openapi-validation --atomic
                                """
                                openshift.raw("start-build ${APP_NAME} --from-repo ./ --follow --wait=true")
                            }
                        }
                    }
                }
            }
        }
    } // STAGES
} // PIPELINE
