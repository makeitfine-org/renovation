{{- define "ca.name" -}}
{{ .Values.name }}
{{- end }}


{{- define "ca.metadata" -}}
metadata:
  name: {{ include "ca.name" . }}{{- if .nameSuffix }}-{{ .nameSuffix }}{{- end }}
  namespace: {{ .Values.namespaceOverride | default .Release.Namespace }}
{{- end }}


{{- define "ca.fullImage" -}}
{{ .Values.image.repository }}:{{ .Values.image.tag }}
{{- end }}


{{/*define template for deployment*/}}
{{- define "ca.deployment" -}}
apiVersion: apps/v1
kind: Deployment
{{ include "ca.metadata" . | nindent 0 }}
spec:
  replicas: 1 # todo: up to 3 and check
  selector:
    matchLabels:
      app: {{ include "ca.name" . }}
  template:
    metadata:
      labels:
        app: {{ include "ca.name" . }}
    spec:
      containers:
        - name: {{ include "ca.name" . | quote }}
          image: {{ include "ca.fullImage" . | quote }}
          imagePullPolicy: {{ .Values.image.pullPolicy | quote }}
          envFrom:
            - configMapRef:
                name: {{ .Values.global.parentChartName }}-configmap
            - secretRef:
                name: {{ .Values.global.parentChartName }}-secret
          ports:
            - containerPort: {{ .containerPort }}
{{- end }}


{{/*define template for service*/}}
{{- define "ca.service" -}}
apiVersion: v1
kind: Service
{{ include "ca.metadata" . | nindent 0 }}
spec:
  selector:
    app: {{ .Values.name }}
  type: {{ .Values.service.type }}
  ports:
    - port: {{ .Values.service.port }}
      targetPort: {{ .targetPort }}
      nodePort: {{ .Values.service.nodePort }}
{{- end }}



{{/*metadata:*/}}
{{/*  name: {{ include "mychart.fullname" . }}*/}}

{{ define "mychart.fullname" -}}
{{- printf "%s-%s" .Release.Name .Chart.Name | trunc 63 | trimSuffix "-" -}}
{{- end -}}


{{- define "mychart.fullImage" -}}
{{ .Values.image.repository }}:{{ .Values.image.tag }}
{{- end -}}


{{/*metadata:*/}}
{{/*  labels:*/}}
{{/*    {{ include "mychart.labels" . | nindent 4 }}*/}}

{{- define "mychart.labels" -}}
app.kubernetes.io/name: {{ .Chart.Name }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/version: {{ .Chart.AppVersion }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}


{{/*spec:*/}}
{{/*  selector:*/}}
{{/*    matchLabels:*/}}
{{/*      {{ include "mychart.selectorLabels" . | nindent 6 }}*/}}

{{- define "mychart.selectorLabels" -}}
app.kubernetes.io/name: {{ .Chart.Name }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}


{{/*spec:*/}}
{{/*  containers:*/}}
{{/*    - name: {{ .Chart.Name }}*/}}
{{/*      image: {{ include "mychart.image" . }}*/}}

{{- define "mychart.image" -}}
{{ .Values.image.repository }}:{{ default .Chart.AppVersion .Values.image.tag }}
{{- end -}}


{{/*Helm will render the ConfigMap,*/}}
{{/*metadata:*/}}
{{/*  labels:*/}}
{{/*    {{ include "annotation.reread.configmap" . | nindent 2 }}*/}}

{{- define "annotation.reread.configmap" -}}
annotations:
  checksum/config: {{ include (print $.Template.BasePath "/configmap.yaml") . | sha256sum }}
{{- end -}}





